package com.iflytek.demo.download;

import android.util.Log;

/**
 * Log统一管理
 * @author Administrator
 *
 */
public class LogUtil {
	private static boolean isDebug = true;//上次市场前把这个置为false
	
	/**
	 * 打印i级别的log
	 * @param tag
	 * @param msg
	 */
	public static void i(String tag,String msg){
		if(isDebug){
			Log.i(tag, msg);
		}
	}
	
	/**
	 * 使用object的类名作为tag
	 * @param object
	 * @param msg
	 */
	public static void i(Object object,String msg){
		if(isDebug){
			Log.i(object.getClass().getSimpleName(), msg);
		}
	}
	
	/**
	 * 打印e级别的log
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag,String msg){
		if(isDebug){
			Log.e(tag, msg);
		}
	}
	
	/**
	 * 使用object的类名作为tag
	 * @param object
	 * @param msg
	 */
	public static void e(Object object,String msg){
		if(isDebug){
			Log.e(object.getClass().getSimpleName(), msg);
		}
	}
}
