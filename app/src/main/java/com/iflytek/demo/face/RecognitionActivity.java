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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.iflytek.demo.R;
import com.iflytek.demo.db.dao.FaceDao;
import com.iflytek.demo.db.entity.Face;
import com.iflytek.demo.recyclerviewdemo.FullyGridLayoutManager;
import com.iflytek.demo.view.CameraPreview;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 人脸比对
 *
 * @author admin
 *
 */
public class RecognitionActivity extends Activity implements OnClickListener {
    public static final String TAG = "RecognitionActivity";
    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout mCameralayout;
    private Button mTakePictureBtn;
    String caremaPhotoPath;
    private LocalSDK mFaceSDK = null;
    FeatureBean mFiledFeatureBean = null;
    RecognitionRecyclerAdapter recycleAdapter;
    LinearLayoutManager layoutManager;
    private FaceDao faceDao;
    ArrayList<Itembean> mDatas;
    private RecyclerView recyclerView;
    private EditText et_enter_cardNumber;
    private String enter_cardNumber;
    private ImageView back_capture;
    private BroadcastReceiveForRecogn receiver;
    private ImageView back_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.recognition_activity);
        recyclerView = (RecyclerView)findViewById(R.id.rv_recognition);
        back_capture = (ImageView)findViewById(R.id.back_capture);
        back_home = (ImageView)findViewById(R.id.back_home);
        back_home.setOnClickListener(this);
        back_capture.setOnClickListener(this);
        et_enter_cardNumber = (EditText)findViewById(R.id.et_enter_cardNumber);
        initData();
        recycleAdapter= new RecognitionRecyclerAdapter(this , mDatas );
        layoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        layoutManager.setOrientation(FullyGridLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        //layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        recyclerView.setAdapter(recycleAdapter);
        //分隔行
        //recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        faceDao = new FaceDao(this);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        ConStant.publicFilePath = new StringBuilder(sdPath).append(File.separator).append("Faceimg")
                .append(File.separator).append(sdf.format(new Date())).toString();
        FileUtil.mkDir(ConStant.publicFilePath);
        caremaPhotoPath = sdPath + "/temp.jpg";
        //更新界面
        if (!checkCameraHardware(this)) {
            Toast.makeText(RecognitionActivity.this, "相机不支持", Toast.LENGTH_SHORT)
                    .show();
        } else {
            openCamera();
            mTakePictureBtn = (Button) findViewById(R.id.button_capture);
            mTakePictureBtn.setOnClickListener(this);
        }
    }
    @Override
    protected void onPause() {
        releaseCamera();
        super.onPause();
    }
    @Override
    protected void onResume() {
        if (!checkCameraHardware(this)) {
            Toast.makeText(RecognitionActivity.this, "相机不支持", Toast.LENGTH_SHORT)
                    .show();
        } else {
            if(mCamera ==null){
                openCamera();
                mTakePictureBtn = (Button) findViewById(R.id.button_capture);
                mTakePictureBtn.setOnClickListener(this);
            }
        }
        mFaceSDK = LocalSDK.getInstance(this);
        receiver = new BroadcastReceiveForRecogn();
        //注册广播接收者,需要一个意图对象,也需要action参数,这里是定义Action参数
        //TODO
        IntentFilter filter = new IntentFilter();
        filter.addAction("net.bunnytouch.bunnyad.service.GetCardNumReceiver");
        filter.addAction("codingpark.net.bunnyad.service.GetCardNumReceiver");
        //注册广播,
        registerReceiver(receiver, filter);
        super.onResume();
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
                mPreview = new CameraPreview(RecognitionActivity.this, mCamera);
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
    private void initData() {
        mDatas = new ArrayList<Itembean>();
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
        mFiledFeatureBean = mFaceSDK.GetFeatureFromImgData(imgData, true);

        if (mFiledFeatureBean.ret != 0) {
            makeToast("人脸特征识别失败，请重新拍照。");
            Log.e(TAG,"probe特征失败:" + mFiledFeatureBean.ret);
        }else {
            List<Face> list = faceDao.queryBycardNumber(enter_cardNumber);
            if(list.size()==0){
                makeToast("此卡未录入，请先录入。");
            }else {
                Face face = list.get(0);
                byte[] facebyte = face.getBytes();
                VerifyBean mVerifyBean = mFaceSDK.Verify(facebyte, mFiledFeatureBean.btFeature);
                Itembean itembean = new Itembean();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                String item_time = simpleDateFormat.format(new Date());
                itembean.setBitmap(bitmap);
                itembean.setData(item_time);
                if(mDatas.size()>5){
                    layoutManager.setStackFromEnd(true);
                }
                if (mVerifyBean.ret == 0) {
                    if(mVerifyBean.score>0.9){
                        Log.d(TAG,mVerifyBean.score+"");
                        itembean.setResult(true);
                        itembean.setState(face.getName()+" 您好！ 签到成功！");
                        mDatas.add(itembean);
                        recycleAdapter.notifyDataSetChanged();
                        FileUtil.deleteFile(new File(path));
                    }else {
                        /*itembean.setResult(false);
                        itembean.setState(face.getName()+" 您好！ 签到失败！");
                        mDatas.add(itembean);
                        recycleAdapter.notifyDataSetChanged();*/
                        makeToast("非本人，无法签到。");
                        FileUtil.deleteFile(new File(path));
                    }

                } else {
                    makeToast("人脸比对失败");
                }
            }
        }

    }

    public void	makeToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_capture:
                //拍照
                // mCamera.autoFocus(mAutoFocusCallback);
                enter_cardNumber = et_enter_cardNumber.getText().toString();
                takeCamera(enter_cardNumber);
                break;
            case R.id.back_capture:
                Intent intent = new Intent(this, EnterActivity.class);
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
    public void takeCamera(String cardNumber){
        if(cardNumber.isEmpty()){
            makeToast("请输入卡号！");
        }else {
            List<Face> list = faceDao.queryBycardNumber(cardNumber);
            if(list.size()==0){
                makeToast("此卡未录入，请先录入。");
            }else{
                mCamera.takePicture(null, null, mPictureCallback);
            }
        }
    }
    public class BroadcastReceiveForRecogn extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String cardnum = intent.getStringExtra("cardnum");
            Log.e("Receiver", intent.getStringExtra("cardnum"));
            et_enter_cardNumber.setText(cardnum);
            enter_cardNumber = cardnum;
            takeCamera(cardnum);
        }
    }
}