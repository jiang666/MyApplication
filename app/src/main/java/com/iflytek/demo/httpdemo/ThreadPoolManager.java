package com.iflytek.demo.httpdemo;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 线程池管理
 * 
 * @author Administrator
 * 
 */
public class ThreadPoolManager {
	private static ThreadPoolManager mInstance = new ThreadPoolManager();
	
	private ThreadPoolExecutor executor;
	private int corePoolSize;//核心线程数
	private int maximumPoolSize;//
	private int keepAliveTime = 1;
	private TimeUnit unit = TimeUnit.HOURS;//
	public static ThreadPoolManager getInstance() {
		return mInstance;
	}

	private ThreadPoolManager() {
		corePoolSize = Runtime.getRuntime().availableProcessors()*2 + 1;//cpu数量*2 + 1
		maximumPoolSize = corePoolSize;
		//初始化线程池
		executor = new ThreadPoolExecutor(
				corePoolSize, 
				maximumPoolSize, 
				keepAliveTime, 
				unit, 
				new LinkedBlockingDeque<Runnable>(),//缓冲队列
				Executors.defaultThreadFactory(), 
				new ThreadPoolExecutor.AbortPolicy());//超过maximumPoolSize的任务的处理策略
	}
	
	/**
	 * 执行任务
	 * @param runnable
	 */
	public void execute(Runnable runnable){
		if(runnable==null)return;
		executor.execute(runnable);
	}
	
	/**
	 * 取消任务
	 * @param runnable
	 */
	public void cancel(Runnable runnable){
		if(runnable==null)return;
		executor.remove(runnable);
	}

}
