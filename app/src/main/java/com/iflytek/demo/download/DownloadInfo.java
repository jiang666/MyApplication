package com.iflytek.demo.download;

/**
 * 封装下载任务的信息
 * @author Administrator
 *
 */
public class DownloadInfo {
	
	private int id;//下载任务的标识
	private long size;//对应apk的大小
	private String downloadUrl;//下载地址
	private long currentLength;//已经下载的大小
	private int state;//下载状态
	private String path;//下载文件的保存路径
	
	/**
	 * 根据appInfo生成一个downloadinfo
	 * @param appInfo
	 * @return
	 */
	public static DownloadInfo create(AppInfo appInfo){
		DownloadInfo downloadInfo = new DownloadInfo();
		downloadInfo.setId(appInfo.getId());
		downloadInfo.setSize(appInfo.getSize());
		downloadInfo.setDownloadUrl(appInfo.getDownloadUrl());
		downloadInfo.setCurrentLength(0);//设置已经下载的长度为0
		downloadInfo.setState(DownloadManager.STATE_NONE);//设置状态为none
		
		//  /mnt/sdcard/com.heima61.googleplay/download/有缘网.apk
		downloadInfo.setPath(DownloadManager.DOWNLOAD_DIR+"/"+appInfo.getName()+".apk");
		return downloadInfo;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getCurrentLength() {
		return currentLength;
	}
	public void setCurrentLength(long currentLength) {
		this.currentLength = currentLength;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	
}

