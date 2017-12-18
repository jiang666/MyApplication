package com.iflytek.demo.face;

/**
 * Created by jianglei on 2017/12/13.
 */


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.demo.R;

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
public class FACEactivity extends Activity implements OnClickListener {
    Button next_btn;
    String caremaPhotoPath;
    TextView faceResult, idcard1_tip, idcard2_tip;
    ImageButton idcard1_btn, idcard2_btn;
    private LocalSDK mFaceSDK = null;
    FeatureBean mFiledFeatureBean = null;
    FeatureBean mProbeFeatureBean = null;
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onResume() {
        mFaceSDK = LocalSDK.getInstance(this);
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twofaceverfy);
        idcard1_btn = (ImageButton) findViewById(R.id.idcard1_btn);
        idcard1_btn.setOnClickListener(this);
        idcard2_btn = (ImageButton) findViewById(R.id.idcard2_btn);
        idcard2_btn.setOnClickListener(this);
        next_btn = (Button) findViewById(R.id.next_btn);
        next_btn.setOnClickListener(this);
        faceResult = (TextView) findViewById(R.id.faceResult);
        idcard1_tip = (TextView) findViewById(R.id.idcard1_tip);
        idcard2_tip = (TextView) findViewById(R.id.idcard2_tip);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        ConStant.publicFilePath = new StringBuilder(sdPath).append(File.separator).append("Faceimg")
                .append(File.separator).append(sdf.format(new Date())).toString();
        FileUtil.mkDir(ConStant.publicFilePath);

        caremaPhotoPath = sdPath + "/temp.jpg";

    }

    public void	makeToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.idcard1_btn:
                showAlertDialog(1, 2);

                break;
            case R.id.idcard2_btn:
                showAlertDialog(3, 4);

                break;
            case R.id.next_btn:
                if (mFiledFeatureBean == null || mFiledFeatureBean.ret != 0) {
                    faceResult.setText("第二张图片未提取特征");
                    break;
                }

                if (mProbeFeatureBean == null || mProbeFeatureBean.ret != 0) {
                    faceResult.setText("第一张图片未提取特征");
                    break;
                }

                startTime = System.currentTimeMillis();
                VerifyBean mVerifyBean = mFaceSDK.Verify(mProbeFeatureBean.btFeature, mFiledFeatureBean.btFeature);
                if (mVerifyBean.ret == 0) {
                    faceResult.setText("相似度：" + mVerifyBean.score + "\n比对耗时(毫秒):" + (System.currentTimeMillis() - startTime));
                } else {
                    faceResult.setText("人脸比对错误：" + mVerifyBean.ret);
                }
                break;
        }

    }

    private void showAlertDialog(final int caneraflag, final int photoflag) {

        new AlertDialog.Builder(this).setNegativeButton("照相", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                takePhoto(caneraflag);
                dialog.dismiss();

            }
        }).setNeutralButton("相册", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                startImgIntent(photoflag);

                dialog.dismiss();

            }
        }).show();
    }

    private void startImgIntent(int requestCode) {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"), requestCode);
        } else {
            intent = new Intent(Intent.ACTION_PICK, Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"), requestCode);
        }
    }

    // Uri imageUri;

    protected void takePhoto(int requestCode) {
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(caremaPhotoPath)));
        startActivityForResult(it, requestCode);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK) {
            return;
        }

        String imgFilePath = null;

        if (requestCode % 2 == 0) {
            if (data == null)
                return;

            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                imgFilePath = ImgUtil.getPath(this, selectedImageUri);
            }

        } else {

            imgFilePath = caremaPhotoPath;
        }
        if (imgFilePath != null) {
            try {
                updateResult(requestCode, imgFilePath);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    long startTime;

    private void updateResult(int requestCode, String imgFilePath) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH_mm_ss");
        String path = ConStant.publicFilePath + "/veryfy" + sdf.format(new Date()) + ".jpg";
        Bitmap bitmapZoom = ImgUtil.zoomPic(imgFilePath, 640, 480, Bitmap.Config.ARGB_8888);
        if (bitmapZoom == null) {
            idcard1_tip.setText("图片数据有问题");
            return;
        }
        ImgUtil.saveJPGE_After(bitmapZoom, path, 80);
        Bitmap bitmap = ImgUtil.getBitmapByPath(path);

        if (requestCode <= 2) {

            // TODO 提取图片probe特征
            byte[] imgData = FileUtil.file2byte(new File(path));
            startTime = System.currentTimeMillis();
            mProbeFeatureBean = mFaceSDK.GetFeatureFromImgData(imgData, false);
            Log.e("-----------",mProbeFeatureBean.btFeature.length+""+new String(mProbeFeatureBean.btFeature));
            FileUtil.writeByteArrayToFile(mProbeFeatureBean.btFeature,ConStant.publicFilePath+"/1123.txt");
            if (mProbeFeatureBean.ret != 0) {
                idcard1_tip.setText("probe特征失败:" + mProbeFeatureBean.ret);
            }
            else {
                idcard1_tip.setText("probe特征耗时(毫秒):" + (System.currentTimeMillis() - startTime));
            }

            idcard1_btn.setImageBitmap(bitmap);
        } else {

            // TODO 提取图片filed特征
            byte[] imgData = FileUtil.file2byte(new File(path));
            startTime = System.currentTimeMillis();
            mFiledFeatureBean = mFaceSDK.GetFeatureFromImgData(imgData, true);
            if (mFiledFeatureBean.ret != 0) {
                idcard2_tip.setText("filed特征失败:" + mFiledFeatureBean.ret);
            }
            else {
                idcard2_tip.setText("filed特征耗时(毫秒):" + (System.currentTimeMillis() - startTime));
            }

            idcard2_btn.setImageBitmap(bitmap);
        }

    }

}