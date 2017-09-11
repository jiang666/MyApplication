package com.iflytek.demo.download;


import android.app.Application;
import android.content.Context;
import android.os.Handler;

public class GooglePlayApplication extends Application{
	private static Context mContext;//项目中公用的上下文对象
	private static Handler mainHandler;//主线程的Handler
	@Override
	public void onCreate() {
		super.onCreate();
		
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

		// Initialize ImageLoader with configuration.
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
