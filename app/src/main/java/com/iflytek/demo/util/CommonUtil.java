package com.iflytek.demo.util;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.iflytek.demo.MApplication;

/**
 * 常用方法封装
 * @author Administrator
 *
 */
public class CommonUtil {
	/**
	 * 在主线程中执行runnable
	 * @param runnable
	 */
	public static void runOnUIThread(Runnable runnable){
		MApplication.getMainHandler().post(runnable);
	}
	
	
	/**
	 * 获取字符串资源
	 * @param id
	 * @return
	 */
	public static String getString(int id){
		return MApplication.getContext().getResources().getString(id);
	}
	
	/**
	 * 获取dimens资源
	 * @param id
	 * @return
	 */
	public static float getDimens(int id){
		return MApplication.getContext().getResources().getDimension(id);
	}
	
	/**
	 * 获取图片资源
	 * @param id
	 * @return
	 */
	public static Drawable getDrawable(int id){
		return MApplication.getContext().getResources().getDrawable(id);
	}
	
	/**
	 * 获取字符串数组资源
	 * @param id
	 * @return
	 */
	public static String[] getStringArray(int id){
		return MApplication.getContext().getResources().getStringArray(id);
	}
	
	/**
	 * 将view从父view中移除
	 * @param view
	 */
	public static void removeSelfFromParent(View view){
		if(view!=null){
			ViewParent parent = view.getParent();
			if(parent instanceof ViewGroup){
				ViewGroup group = (ViewGroup) parent;
				group.removeView(view);
			}
		}
	}
}
