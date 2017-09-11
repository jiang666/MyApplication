package com.iflytek.demo.download;

import java.util.ArrayList;

public class AppInfo {
	private int id;//app的唯一标识
	private String name;//app的名称
	private String packageName;//app的包名
	private String iconUrl;//图标url后缀，显示的时候需求拼上服务器host地址
	private float stars;//app的星级
	private long size;//app的大小
	private String downloadUrl;//app的下的载url后缀，下载时需求拼上主机host地址
	private String des;//app的描述
	
	private String downloadNum;//下载数量
	private String version;//当前版本
	private String date;//上传日期
	private String author;//作者
	private ArrayList<String> screen;//app截图
	private ArrayList<SafeInfo> safe;//安全描述
	
	
	public String getDownloadNum() {
		return downloadNum;
	}
	public void setDownloadNum(String downloadNum) {
		this.downloadNum = downloadNum;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public ArrayList<String> getScreen() {
		return screen;
	}
	public void setScreen(ArrayList<String> screen) {
		this.screen = screen;
	}
	public ArrayList<SafeInfo> getSafe() {
		return safe;
	}
	public void setSafe(ArrayList<SafeInfo> safe) {
		this.safe = safe;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public float getStars() {
		return stars;
	}
	public void setStars(float stars) {
		this.stars = stars;
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
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	@Override
	public String toString() {
		return "AppInfo [id=" + id + ", name=" + name + ", packageName="
				+ packageName + ", iconUrl=" + iconUrl + ", stars=" + stars
				+ ", size=" + size + ", downloadUrl=" + downloadUrl + ", des="
				+ des + ", downloadNum=" + downloadNum + ", version=" + version
				+ ", date=" + date + ", author=" + author + ", screen="
				+ screen + ", safe=" + safe + "]";
	}
	
	
	
	
}
