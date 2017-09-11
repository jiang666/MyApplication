package com.iflytek.demo.download;

/**
 * 存放接口url
 * @author Administrator
 *
 */
public class Url {
	public static String HOST = "http://127.0.0.1:8090/";//服务器主机
	public static String IMAGE_PREFIX = HOST +  "image?name=";//图片url前缀
	
	//home页url
	public static String HOME = HOST + "home?index=%1$d";//使用占位符%1$d  1:第一个参数   d:int  s:string
	//http://127.0.0.1:8090/app?index=0
	//app页面的url
	public static String APP = HOST + "app?index=%1$d";
	//游戏页的url
	public static String GAME = HOST + "game?index=%1$d";
	//专题页的url
	public static String SUBJECT = HOST + "subject?index=%1$d";
	//推荐页的url
	public static String RECOMMEND = HOST + "recommend?index=%1$d";
	//分类页的url
	public static String CATEGORY = HOST + "category?index=%1$d";
	//热门页的url
	public static String HOT = HOST + "hot?index=%1$d";
	//详情页的url
	public static String DETAIL = HOST + "detail?packageName=%1$s";
	//下载接口，从头下载
	public static String DOWNLOAD = HOST + "download?name=%1$s";
	//断点下载的接口
	public static String BREAK_DOWNLOAD = HOST + "download?name=%1$s&range=%2$d";
	
	
}
