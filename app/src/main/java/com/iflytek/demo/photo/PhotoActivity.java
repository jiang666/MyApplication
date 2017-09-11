package com.iflytek.demo.photo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.iflytek.demo.R;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jianglei on 2017/9/6.
 */

public class PhotoActivity extends AppCompatActivity  {
    private File currentImageFile;
    private ImageView iv_imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity);
        iv_imageView = (ImageView)findViewById(R.id.image);
        findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
    }

    public void takePhoto(){
        File dir = new File(Environment.getExternalStorageDirectory(), "myimage");//在sd下创建文件夹myimage；Environment.getExternalStorageDirectory()得到SD卡路径文件
        if (!dir.exists()) {    //exists()判断文件是否存在，不存在则创建文件
            dir.mkdirs();
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式在android中，创建文件时，文件名中不能包含“：”冒号
        String filename = df.format(new Date());
        currentImageFile = new File(dir, filename + ".jpg");
        if (!currentImageFile.exists()) {
            try{
                currentImageFile.createNewFile();
            }catch(IOException e ){
                System.out.println("读取失败！");
            }
        }
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentImageFile));
        startActivityForResult(openCameraIntent, 1);
    }

    //图片按比例大小压缩方法（根据路径获取图片并压缩）：
    public Bitmap getSmallBitmap(File file, int reqWidth, int reqHeight) {
        try {
            String filePath = file.getAbsolutePath();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;//开始读入图片，此时把options.inJustDecodeBounds 设回true了
            BitmapFactory.decodeFile(filePath, options);//此时返回bm为空
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);//设置缩放比例
            options.inJustDecodeBounds = false;//重新读入图片，注意此时把options.inJustDecodeBounds 设回false了
            Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
            //压缩好比例大小后不进行质量压缩
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(currentImageFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到bos中
            //压缩好比例大小后再进行质量压缩
            //compressImage(bitmap,filePath);
            return bitmap;
        } catch (Exception e) {
            Log.d("wzc", "类:" + this.getClass().getName() + " 方法：" + Thread.currentThread()
                    .getStackTrace()[0].getMethodName() + " 异常 " + e);
            return null;
        }
    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        try {
            int height = options.outHeight;
            int width = options.outWidth;
            int inSampleSize = 1;  //1表示不缩放
            if (height > reqHeight || width > reqWidth) {
                int heightRatio = Math.round((float) height / (float) reqHeight);
                int widthRatio = Math.round((float) width / (float) reqWidth);
                inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            }
            return inSampleSize;
        } catch (Exception e) {
            Log.d("wzc", "类:" + this.getClass().getName() + " 方法：" + Thread.currentThread()
                    .getStackTrace()[0].getMethodName() + " 异常 " + e);
            return 1;
        }
    }

    // 质量压缩法：
    private Bitmap compressImage(Bitmap image, String filepath) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 100;
            while (baos.toByteArray().length / 1024 > 500) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                baos.reset();//重置baos即清空baos
                options -= 10;//每次都减少10
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

            }
            //压缩好后写入文件中
            FileOutputStream fos = new FileOutputStream(filepath);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.i("TestFile", "SD card is not avaiable/writeable right now.");
                return;
            }
            //原图
            String filePath = currentImageFile.getAbsolutePath();
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            compressImage(bitmap,filePath);
            //利用Bitmap对象创建缩略图
            Bitmap showbitmap = ThumbnailUtils.extractThumbnail(bitmap, 250, 250);
            iv_imageView.setImageBitmap(showbitmap);
        }
    }
}
