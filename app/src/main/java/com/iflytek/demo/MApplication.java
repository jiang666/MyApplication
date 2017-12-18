package com.iflytek.demo;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.iflytek.demo.util.CrashHandlerUtil;
import com.iflytek.demo.util.LogUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MApplication extends Application{
	private static Context mContext;//项目中公用的上下文对象
	private static Handler mainHandler;//主线程的Handler
	public static boolean LOG = true;

	@Override
	public void onCreate() {
		super.onCreate();
		LogUtil.setLog(false);
		//崩溃处理
		CrashHandlerUtil crashHandlerUtil = CrashHandlerUtil.getInstance();
		crashHandlerUtil.init(this);
		crashHandlerUtil.setCrashTip("很抱歉，程序出现异常，即将退出！");
		mContext = this;
		mainHandler = new Handler();
		
		initImageLoader(mContext);
	}
	
	/**
	 * 初始化ImageLoader
	 * @param context
	 */
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		config.threadPriority(Thread.NORM_PRIORITY - 2);//设置线程优先级
		config.denyCacheImageMultipleSizesInMemory();//拒绝保存多分不同size的image在内存中
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());//设置硬盘缓存文件的名字如何生成
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs(); // Remove for release app

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config.build());
	}
	
	/**
	 * 获取全局上下文对象
	 * @return
	 */
	public static Context getContext(){
		return mContext;
	}
	
	/**
	 * 获取主线程的Handler
	 * @return
	 */
	public static Handler getMainHandler(){
		return mainHandler;
	}
}
