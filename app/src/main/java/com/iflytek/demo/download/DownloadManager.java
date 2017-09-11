package com.iflytek.demo.download;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.iflytek.demo.httpdemo.ThreadPoolManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 下载管理器
 * @author Administrator
 *
 */
public class DownloadManager {
	//定义下载目录     /mnt/sdcard/com.heima61.googleplay/download
	public static String DOWNLOAD_DIR = Environment.getExternalStorageDirectory()
			+"/"+GooglePlayApplication.getContext().getPackageName()+"/download";
	
	//定义下载状态
	public static final int STATE_NONE = 0;//初始化状态
	public static final int STATE_DOWNLOADING = 1;//下载中的状态
	public static final int STATE_PAUSE = 2;//暂停的状态
	public static final int STATE_WAITTING = 3;//等待的状态
	public static final int STATE_FINISH = 4;//完成下载的状态
	public static final int STATE_ERROR = 5;//下载出错或失败的状态
	
	private static DownloadManager mInstance = new DownloadManager();
	private DownloadManager(){
		//初始化下载目录
		File file = new File(DOWNLOAD_DIR);
		if(!file.exists()){
			file.mkdirs();//创建下载目录
		}
	}
	
//	public static SparseArray<DownloadInfo> sparseArray = new SparseArray<DownloadInfo>();
	/**
	 * 用来保存下载信息的，此时只是将下载信息保存到内存中，并没有持久化保存,真实项目最好持久化保存(存到数据库)
	 */
	private HashMap<Integer, DownloadInfo> downloadInfoMap = new HashMap<Integer, DownloadInfo>();
	/**
	 * 用来存放下载任务的，当暂停的时候从里面根据id取出任务，然后从线程池移除
	 */
	private HashMap<Integer, DownloadTask> downloadTaskMap = new HashMap<Integer, DownloadTask>();
	/**
	 * 由于我们需要多个界面同时更新UI，也就是说多个界面都需要注册DownloadObserver，所以用observerList来保存
	 */
	private ArrayList<DownloadObserver> observerList = new ArrayList<DownloadObserver>();
	
	public static DownloadManager getInstance(){
		return mInstance;
	}
	
	/**
	 * 根据appInfo的id获取对应的downloadInfo
	 * @param appInfo
	 * @return
	 */
	public DownloadInfo getDownloadInfo(AppInfo appInfo){
		if(appInfo!=null){
			return downloadInfoMap.get(appInfo.getId());
		}else {
			return null;
		}
	}
	
	/**
	 * 下载方法
	 */
	public void download(AppInfo appInfo){
		//1.获取下载信息
		DownloadInfo downloadInfo = downloadInfoMap.get(appInfo.getId());
		if(downloadInfo==null){
			downloadInfo = DownloadInfo.create(appInfo); 
			downloadInfoMap.put(appInfo.getId(), downloadInfo);//将downloadInfo维护起来
		}
		//2.判断状态，是否能够下载，如果是none，pause，error状态都可以开始下载
		if(downloadInfo.getState()==STATE_NONE || downloadInfo.getState()==STATE_PAUSE
				|| downloadInfo.getState()==STATE_ERROR){
			//可以开始下载了，创建下载任务
			DownloadTask downloadTask = new DownloadTask(downloadInfo);
			downloadTaskMap.put(downloadInfo.getId(), downloadTask);//将下载任务维护起来
			
			//将downloadInfo状态更新为waitting，因为还没真正开始下载，当执行run方法时才是真正下载
			downloadInfo.setState(STATE_WAITTING);
			//需要通知所有UI更新界面
			notifyDownloadStateChange(downloadInfo);
			
			//真正使用线程池维护DownloadTask，进行下载
			ThreadPoolManager.getInstance().execute(downloadTask);
		}
	}
	
	/**
	 * 下载任务
	 * @author Administrator
	 *
	 */
	class DownloadTask implements Runnable{
		private DownloadInfo downloadInfo;
		public DownloadTask(DownloadInfo downloadInfo) {
			this.downloadInfo = downloadInfo;
		}

		@Override
		public void run() {
			//3.一旦走到这里，说明下载任务开始执行，也就是说当前下载信息的状态应该变为下载中
			downloadInfo.setState(STATE_DOWNLOADING);
			notifyDownloadStateChange(downloadInfo);
			
			//4.真正开始下载，2种情况：a.从头下载   b.断点下载
			File file = new File(downloadInfo.getPath());//下载文件
			HttpHelper.HttpResult httpResult;
			if(!file.exists() || file.length()!=downloadInfo.getCurrentLength()){
				//文件不存在，或者文件无效
				file.delete();//删除无效文件
				downloadInfo.setCurrentLength(0);//重置保存的已经下载的大小
				//从头下载
				String url = String.format(Url.DOWNLOAD, downloadInfo.getDownloadUrl());
				httpResult = HttpHelper.download(url);
			}else {
				//断点下载
				String url = String.format(Url.BREAK_DOWNLOAD,  downloadInfo.getDownloadUrl(),downloadInfo.getCurrentLength());
				httpResult = HttpHelper.download(url);
			}
			
			//5.读取流，写入文件
			if(httpResult!=null && httpResult.getInputStream()!=null){
				FileOutputStream fos = null;
				try {
					InputStream is = httpResult.getInputStream();
					fos = new FileOutputStream(file, true);
					byte[] buffer = new byte[1024*10];//使用10k的缓冲区
					int len = -1;//记录本次读到的长度
					while((len=is.read(buffer))!=-1 && downloadInfo.getState()==STATE_DOWNLOADING){
						fos.write(buffer, 0, len);
						fos.flush();
						//通知进度更新，并且更新currentLength
						downloadInfo.setCurrentLength(downloadInfo.getCurrentLength()+len);
						notifyDownloadProgressChange(downloadInfo);
					}
				} catch (Exception e) {
					e.printStackTrace();
					//进入下载失败状态
					file.delete();//删除无效文件
					downloadInfo.setCurrentLength(0);//重置保存的已经下载的长度
					downloadInfo.setState(STATE_ERROR);
					notifyDownloadStateChange(downloadInfo);
				}finally{
					//关闭流和链接
					try {
						if(fos!=null){
							fos.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					httpResult.close();
				}
				
				
				//for循环结束之后走到这里 ，3种情况：a.下载完成    b.暂停     c.下载失败
				if(file.length()==downloadInfo.getSize() && downloadInfo.getState()==STATE_DOWNLOADING){
					//变为下载完成状态
					downloadInfo.setState(STATE_FINISH);
					notifyDownloadStateChange(downloadInfo);
				}else if (downloadInfo.getState()==STATE_PAUSE) {
					notifyDownloadStateChange(downloadInfo);//通知UI更新
				}else if (file.length()!=downloadInfo.getCurrentLength()) {
					//属于下载失败的情况
					file.delete();//删除无效文件
					downloadInfo.setCurrentLength(0);//重置保存的已经下载的长度
					downloadInfo.setState(STATE_ERROR);
					notifyDownloadStateChange(downloadInfo);
				}
				
			}else {
				//进入下载失败状态
				file.delete();//删除无效文件
				downloadInfo.setCurrentLength(0);//重置保存的已经下载的长度
				downloadInfo.setState(STATE_ERROR);
				notifyDownloadStateChange(downloadInfo);
			}
			
			//由于下载task已经结束，所以不需要维护该任务，线程池会自动remove该任务
			downloadTaskMap.remove(downloadInfo.getId());
		}
		
	}
	
	/**
	 * 暂停下载的操作
	 */
	public void pause(AppInfo appInfo){
		DownloadInfo downloadInfo = downloadInfoMap.get(appInfo.getId());
		if(downloadInfo!=null){
			//更新状态
			downloadInfo.setState(STATE_PAUSE);
			notifyDownloadStateChange(downloadInfo);
			
			DownloadTask downloadTask = downloadTaskMap.get(downloadInfo.getId());
			//取出任务，从线程池中移除
			if(downloadTask!=null){
				ThreadPoolManager.getInstance().cancel(downloadTask);
			}
		}
	}
	
	/**
	 * 安装apk
	 * @param
	 */
	public void installApk(DownloadInfo downloadInfo){
		//<action android:name="android.intent.action.VIEW" />
        //<category android:name="android.intent.category.DEFAULT" />
        //<data android:scheme="content" />
        //<data android:scheme="file" />
        //<data android:mimeType="application/vnd.android.package-archive" />
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//需要另外开任务栈
		intent.setDataAndType(Uri.parse("file://"+downloadInfo.getPath()), "application/vnd.android.package-archive");
		GooglePlayApplication.getContext().startActivity(intent);
	}
	
	
	/**
	 * 通知所有的监听器下载状态改变了
	 */
	private void notifyDownloadStateChange(DownloadInfo downloadInfo){
		for(DownloadObserver observer : observerList){
			observer.onDownloadStateChange(downloadInfo);
		}
	}
	
	/**
	 * 通知所有的监听器下载进度更新
	 * @param downloadInfo
	 */
	private void notifyDownloadProgressChange(DownloadInfo downloadInfo){
		for(DownloadObserver observer : observerList){
			observer.onDownloadProgressChange(downloadInfo);
		}
	}
	
	/**
	 * 注册下载监听
	 * @param observer
	 */
	public void registerDownloadObserver(DownloadObserver observer){
		if(observer!=null && !observerList.contains(observer)){
			observerList.add(observer);
		}
	}
	/**
	 * 取消下载监听
	 * @param observer
	 */
	public void unregisterDownloadObserver(DownloadObserver observer){
		if(observer!=null && observerList.contains(observer)){
			observerList.remove(observer);
		}
	}
	
	
	/**
	 * 下载监听器
	 * @author Administrator
	 *
	 */
	public interface DownloadObserver{
		/**
		 * 下载状态改变的回调
		 * @param downloadInfo
		 */
		void onDownloadStateChange(DownloadInfo downloadInfo);
		
		/**
		 * 下载进度改变的回调
		 * @param downloadInfo
		 */
		void onDownloadProgressChange(DownloadInfo downloadInfo);
	}
	
}
