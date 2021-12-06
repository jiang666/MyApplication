package com.iflytek.demo.face;

/**
 * Created by jianglei on 2017/12/13.
 */


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
    //定义广播接收者
    BroadcastReceiveForEnter receiver;
    private ImageView back_capture;
    private ImageView back_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.enter_activity);
        iv_enter_image = (ImageView)findViewById(R.id.iv_enter_image);
        et_enter_cardNumber = (EditText)findViewById(R.id.et_enter_cardNumber);
        et_enter_name = (EditText)findViewById(R.id.et_enter_name);
        rb_sex_man = (RadioButton)findViewById(R.id.rb_sex_man);
        rb_sex_woman = (RadioButton)findViewById(R.id.rb_sex_woman);
        back_capture = (ImageView)findViewById(R.id.back_capture);
        back_home = (ImageView)findViewById(R.id.back_home);
        back_home.setOnClickListener(this);
        back_capture.setOnClickListener(this);
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
                /*mPreview = new CameraPreview(EnterActivity.this, mCamera);
                mPreview.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        mCamera.autoFocus(null);
                        return false;
                    }
                });
                mCameralayout = (FrameLayout) findViewById(R.id.camera_preview);
                mCameralayout.addView(mPreview);
                mCamera.startPreview();*/
                mPreview = new CameraPreview(EnterActivity.this, mCamera);
                mCameralayout = (FrameLayout) findViewById(R.id.camera_preview);
                mCameralayout.addView(mPreview);
                ImageView camerImage = new ImageView(this);
                camerImage.setBackground(getResources().getDrawable(R.drawable.camera));
                mCameralayout.addView(camerImage);
            }else {
                makeToast("相机不可用");
            }
        }
    }
    @Override
    protected void onResume() {
        if (!checkCameraHardware(this)) {
            Toast.makeText(EnterActivity.this, "相机不支持", Toast.LENGTH_SHORT)
                    .show();
        } else {
            if(mCamera ==null){
                openCamera();
                mTakePictureBtn = (Button) findViewById(R.id.button_capture);
                mTakePictureBtn.setOnClickListener(this);
            }
        }
        mFaceSDK = LocalSDK.getInstance(this);
        receiver = new BroadcastReceiveForEnter();
        //注册广播接收者,需要一个意图对象,也需要action参数,这里是定义Action参数
        IntentFilter filter = new IntentFilter();
        filter.addAction("net.bunnytouch.bunnyad.service.GetCardNumReceiver");
        filter.addAction("codingpark.net.bunnyad.service.GetCardNumReceiver");
        //注册广播,
        registerReceiver(receiver, filter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        releaseCamera();
        unregisterReceiver(receiver);
        super.onPause();
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
                // mCamera.autoFocus(mAutoFocusCallback);
                mCamera.takePicture(null, null, mPictureCallback);
                break;
            case R.id.btn_affirm:
                //确认录入
                if(et_enter_cardNumber.getText().toString().isEmpty()||et_enter_name.getText().toString().isEmpty()||mProbeFeatureBean==null
                       ){
                    makeToast("卡号、姓名和图像不能为空！");
                }else if(mProbeFeatureBean.ret != 0){
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
                iv_enter_image.setImageResource(R.drawable.defface);
                break;
            case R.id.rb_sex_man:
                rb_sex_woman.setChecked(false);
                break;
            case R.id.rb_sex_woman:
                rb_sex_man.setChecked(false);
                break;
            case R.id.back_capture:
                Intent intent = new Intent(this, RecognitionActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.back_home:
                this.finish();
                Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
                mHomeIntent.addCategory(Intent.CATEGORY_HOME);
                mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                startActivity(mHomeIntent);
                break;
        }

    }
    // 聚焦回调
    private Camera.AutoFocusCallback mAutoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                mCamera.takePicture(null, null, mPictureCallback);
            }
        }
    };
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    public class BroadcastReceiveForEnter extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String cardnum = intent.getStringExtra("cardnum");
            switch (cardnum) {
                case "0736077566":
                    et_enter_cardNumber.setText("0736077566");
                    et_enter_name.setText("王学文");
                    break;
                case "0736148167":
                    et_enter_cardNumber.setText("0736148167");
                    et_enter_name.setText("石海峰");
                    break;
                case "0736074012":
                    et_enter_cardNumber.setText("0736074012");
                    et_enter_name.setText("解然");
                    break;
                case "0736174253":
                    et_enter_cardNumber.setText("0736174253");
                    et_enter_name.setText("单小熙");
                    break;
                case "0736183497":
                    et_enter_cardNumber.setText("0736183497");
                    et_enter_name.setText("陈江华");
                    break;
                case "0736195674":
                    et_enter_cardNumber.setText("0736195674");
                    et_enter_name.setText("胡祥洪");
                    break;
                case "0736054140":
                    et_enter_cardNumber.setText("0736054140");
                    et_enter_name.setText("杨凡");
                    break;
                case "0736199062":
                    et_enter_cardNumber.setText("0736199062");
                    et_enter_name.setText("蒋磊");
                    break;
                default:
                    et_enter_cardNumber.setText(cardnum);
                    break;
            }
            /*switch (cardnum) {
                case "736077566":
                    et_enter_cardNumber.setText("736077566");
                    et_enter_name.setText("王学文");
                    break;
                case "736148167":
                    et_enter_cardNumber.setText("736148167");
                    et_enter_name.setText("石海峰");
                    break;
                case "736074012":
                    et_enter_cardNumber.setText("736074012");
                    et_enter_name.setText("解然");
                    break;
                case "736174253":
                    et_enter_cardNumber.setText("736174253");
                    et_enter_name.setText("单小熙");
                    break;
                case "736183497":
                    et_enter_cardNumber.setText("736183497");
                    et_enter_name.setText("陈江华");
                    break;
                case "736195674":
                    et_enter_cardNumber.setText("736195674");
                    et_enter_name.setText("胡祥洪");
                    break;
                case "736054140":
                    et_enter_cardNumber.setText("736054140");
                    et_enter_name.setText("杨凡");
                    break;
                case "736199062":
                    et_enter_cardNumber.setText("736199062");
                    et_enter_name.setText("蒋磊");
                    break;
                default:
                    et_enter_cardNumber.setText(cardnum);
                    break;
            }*/
        }
    }

}