package com.iflytek.demo.download;

import com.loopj.android.http.HttpGet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Http模块
 * @author Administrator
 *
 */
public class HttpHelper {
	private static final String tag = "HttpHelper";
	/**
	 * get请求
	 * @param url 请求的地址
	 * @return
	 */
	public static String get(String url){
		LogUtil.e(tag, "请求的url: "+url);
		String result = "";
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		
		boolean retry = true;//增加重试(重新请求)机制
		
		while(retry){
			try {
				HttpResponse httpResponse = httpClient.execute(httpGet);
				if(httpResponse.getStatusLine().getStatusCode()<300){
					//服务器响应成功
					HttpEntity entity = httpResponse.getEntity();//拿到http响应体
					InputStream is = entity.getContent();//拿到输入流
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024*4];//4k作为缓存区
					
					int len = -1;//用来记录每次循环读取的长度
					while((len=is.read(buffer))!=-1){
						baos.write(buffer, 0, len);
						baos.flush();//保证能够完全从缓存区刷到输出流
					}
					result = new String(baos.toByteArray());
					
					//关闭流和链接
					is.close();
					baos.close();
					//及时关闭链接
					httpClient.getConnectionManager().closeExpiredConnections();
					
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				retry = false;//如果出异常，则中断循环
			}
		}
		LogUtil.e(tag, "response: "+result);
		return result;
	}
	
	/**
	 * 下载文件，返回流对象
	 * 
	 * @param url
	 * @return
	 */
	public static HttpResult download(String url) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		boolean retry = true;
		while (retry) {
			try {
				HttpResponse httpResponse = httpClient.execute(httpGet);
				if(httpResponse!=null){
					return new HttpResult(httpClient, httpGet, httpResponse);
				}
			} catch (Exception e) {
				retry = false;
				e.printStackTrace();
				LogUtil.e(tag, "download: "+e.getMessage());
			}
		}
		return null;
	}

	/**
	 * Http返回结果的进一步封装
	 * @author Administrator
	 *
	 */
	public static class HttpResult {
		private HttpClient httpClient;
		private HttpGet httpGet;
		private HttpResponse httpResponse;
		private InputStream inputStream;

		public HttpResult(HttpClient httpClient, HttpGet httpGet,
				HttpResponse httpResponse) {
			super();
			this.httpClient = httpClient;
			this.httpGet = httpGet;
			this.httpResponse = httpResponse;
			
			
		}

		/**
		 * 获取状态码
		 * @return
		 */
		public int getStatusCode() {
			StatusLine status = httpResponse.getStatusLine();
			return status.getStatusCode();
		}

		/**
		 * 获取输入流
		 * @return
		 */
		public InputStream getInputStream(){
			if(inputStream==null && getStatusCode()<300){
				HttpEntity entity = httpResponse.getEntity();
				try {
					inputStream =  entity.getContent();
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.e(this, "getInputStream: "+e.getMessage());
				}
			}
			return inputStream;
		}

		/**
		 * 关闭链接和流对象
		 */
		public void close() {
			if (httpGet != null) {
				httpGet.abort();
			}
			if(inputStream!=null){
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
					LogUtil.e(this, "close: "+e.getMessage());
				}
			}
			//关闭链接
			if (httpClient != null) {
				httpClient.getConnectionManager().closeExpiredConnections();
			}
		}
	}
}
