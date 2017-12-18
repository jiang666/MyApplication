package com.iflytek.demo.face;

import android.os.Environment;

import java.io.File;

public class ConStant {

	public static String publicFilePath;
	
	// 模型文件夹名
	public static final String ASSERT_MODULE_DIR = "CWModels";
	
	// 模型路径，建议将模型文件夹CWModels放置到sdcard根目录
	public static String sModelDir= Environment.getExternalStorageDirectory() + File.separator + "CWModels";
	
	// 授权码 ，由云从科技提供，也可调用网络授权接口cwGetLicence获取
	public static String sLicence="NDkxNjExbm9kZXZpY2Vjd2F1dGhvcml6Zf7n4ubm5uXi3+fg5efm5Ob/5ebm4Obg5Yjm5uvl5ubrkeXm5uvl5uai6+Xm5uvl5uTm6+Xm5uDm1efr5+vn6+er4Ofr5+vn67/n5uXm5ubl";
	
	// 人脸检测最小最大人脸
	public static int faceMinSize=30, faceMaxSize=400;
}
