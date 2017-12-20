package com.iflytek.demo.face;

/**
 * Created by jianglei on 2017/12/13.
 */


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.iflytek.demo.R;
import com.iflytek.demo.db.dao.FaceDao;
import com.iflytek.demo.db.entity.Face;
import com.iflytek.demo.view.CameraPreview;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 人脸比对
 *
 * @author admin
 *
 */
public class EnterActivity extends Activity implements OnClickListener {
    public static final String TAG = "CameraSimple";
    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout mCameralayout;
    private Button mTakePictureBtn;
    String caremaPhotoPath;
    FeatureBean mProbeFeatureBean = null;
    private LocalSDK mFaceSDK = null;
    private FaceDao faceDao;
    private ImageView iv_enter_image;
    private EditText et_enter_cardNumber;
    private EditText et_enter_name;
    private RadioButton rb_sex_man;
    private RadioButton rb_sex_woman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.enter_activity);
        if (!checkCameraHardware(this)) {
            Toast.makeText(EnterActivity.this, "相机不支持", Toast.LENGTH_SHORT)
                    .show();
        } else {
            openCamera();
            mTakePictureBtn = (Button) findViewById(R.id.button_capture);
            mTakePictureBtn.setOnClickListener(this);
        }
        iv_enter_image = (ImageView)findViewById(R.id.iv_enter_image);
        et_enter_cardNumber = (EditText)findViewById(R.id.et_enter_cardNumber);
        et_enter_name = (EditText)findViewById(R.id.et_enter_name);
        rb_sex_man = (RadioButton)findViewById(R.id.rb_sex_man);
        rb_sex_woman = (RadioButton)findViewById(R.id.rb_sex_woman);
        rb_sex_man.setOnClickListener(this);
        rb_sex_woman.setOnClickListener(this);
        findViewById(R.id.btn_affirm).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        faceDao = new FaceDao(this);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        ConStant.publicFilePath = new StringBuilder(sdPath).append(File.separator).append("Faceimg")
                .append(File.separator).append(sdf.format(new Date())).toString();
        FileUtil.mkDir(ConStant.publicFilePath);

        caremaPhotoPath = sdPath + "/temp.jpg";

    }
    // 开始预览相机
    public void openCamera(){
        if(mCamera == null){
            mCamera = getCameraInstance();
            if(mCamera!=null){
                mPreview = new CameraPreview(EnterActivity.this, mCamera);
                mCameralayout = (FrameLayout) findViewById(R.id.camera_preview);
                mCameralayout.addView(mPreview);
            }else {
                makeToast("相机不可用");
            }
        }
    }
    @Override
    protected void onResume() {
        mFaceSDK = LocalSDK.getInstance(this);
        super.onResume();
    }
    // 拍照回调
    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            FileUtil.writeByteArrayToFile(data,caremaPhotoPath);
            if (caremaPhotoPath != null) {
                try {
                    updateResult(caremaPhotoPath);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_capture:
                //拍照
                mCamera.takePicture(null, null, mPictureCallback);
                break;
            case R.id.btn_affirm:
                //确认录入
                if(et_enter_cardNumber.getText().toString().isEmpty()||et_enter_name.getText().toString().isEmpty()
                       ){
                    makeToast("卡号或者姓名不能为空！");
                }else if(mProbeFeatureBean!=null&&mProbeFeatureBean.ret != 0){
                    makeToast("人脸识别失败，请重新采集数据！");
                }else {
                    String cardNumber = et_enter_cardNumber.getText().toString();
                    String name = et_enter_name.getText().toString();
                    String sex = null;
                    if(rb_sex_man.isChecked()){
                        sex = "man";
                    }else {
                        sex = "woman";
                    }
                    if(faceDao.queryBycardNumber(cardNumber).size()>0){
                        makeToast("此卡号已存在，请确认。");
                    }else {
                        //确定录入时调用并判断 cardnumber 是否存在，存在提示。
                        Face face = new Face(cardNumber, name, sex, mProbeFeatureBean.btFeature);
                        faceDao.addFace(face);
                        makeToast("录入成功");
                    }

                }
                break;
            case R.id.btn_cancel:
                //界面数据清空
                et_enter_cardNumber.setText(null);
                et_enter_name.setText(null);
                mProbeFeatureBean = null;
                iv_enter_image.setImageResource(R.drawable.idbcg2);
                break;
            case R.id.rb_sex_man:
                rb_sex_woman.setChecked(false);
                break;
            case R.id.rb_sex_woman:
                rb_sex_man.setChecked(false);
                break;
        }

    }
    // 判断相机是否支持
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    // 获取相机
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }
    // 释放相机
    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }
    private void updateResult(String imgFilePath) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH_mm_ss");
        String path = ConStant.publicFilePath + "/veryfy" + sdf.format(new Date()) + ".jpg";
        Bitmap bitmapZoom = ImgUtil.zoomPic(imgFilePath, 640, 480, Bitmap.Config.ARGB_8888);
        if (bitmapZoom == null) {
            makeToast("图片数据有问题");
            return;
        }
        ImgUtil.saveJPGE_After(bitmapZoom, path, 80);
        Bitmap bitmap = ImgUtil.getBitmapByPath(path);
            // TODO 提取图片probe特征
            byte[] imgData = FileUtil.file2byte(new File(path));
            mProbeFeatureBean = mFaceSDK.GetFeatureFromImgData(imgData, false);
            if (mProbeFeatureBean.ret != 0) {
                makeToast("人脸特征识别失败，请重试。");
                Log.e(TAG,"probe特征失败:" + mProbeFeatureBean.ret);
            }
            else {
                iv_enter_image.setImageBitmap(bitmap);
            }

    }

    public void	makeToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}