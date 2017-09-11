package com.iflytek.demo.httpdemo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iflytek.demo.R;
import com.iflytek.demo.database.DBOpenHelper;
import com.iflytek.demo.evenbus.MessageEven;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by jianglei on 2017/3/15.
 */

public class NetActivity extends Activity{
    private TextView tv_nethttp;
    private TextView tv_title;
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.net_activity);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("网络");
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetActivity.this.finish();
            }
        });
        tv_nethttp = (TextView) findViewById(R.id.tv_nethttp);
        ImageView targetImageView = (ImageView) findViewById(R.id.vi_nethttp);
        String internetUrl = "http://192.168.1.141/httptest.png";
        Picasso.with(this)
                .load(internetUrl)
                .transform(new CropSquareTransformation())
                .into(targetImageView);
        ObjectAnimator animator = ObjectAnimator.ofFloat(targetImageView, "scaleX", 1f, 2f, 1f);
        animator.setDuration(2000);
        //animator.setRepeatCount(-1);
        animator.start();
        //透明度起始为1，结束时为0
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(targetImageView, "alpha", 0.5f, 1f);
        animator1.setDuration(1000);//时间1s
        animator1.start();
        /*targetImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        // 获取当前触摸点的x,y坐标
                        x = (int) event.getX();
                        y = (int) event.getY();
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return true;
            }
        });*/
        targetImageView.setOnClickListener(new myButtonListener());

        findViewById(R.id.btn_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        download();
                    }
                }).start();
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String file = NetActivity.this.getExternalFilesDir(null) + File.separator+"1504410491915.apk";
                        File file6 = new File(file);
                        if(file6.isFile()) {
                            file6.delete();
                        }
                    }
                }).start();
            }
        });
        findViewById(R.id.btn_adddata).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DBOpenHelper helper = new DBOpenHelper(NetActivity.this);
                // 调用 getWritableDatabase()或者 getReadableDatabase()其中一个方法将数据库建立
                SQLiteDatabase db = helper.getWritableDatabase();  //得到的是SQLiteDatabase对象
                //插入数据SQL语句
                String stu_sql="insert into person(name,address) values('xiaoming','01005')";
                //执行SQL语句
                db.execSQL(stu_sql);
            }
        });
        findViewById(R.id.btn_netcall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEven("--------------------"));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            getWeather();

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

    }
    private void download() {
        AppClient.ApiStores apiStores = AppClient.retrofit().create(AppClient.ApiStores.class);
        Observable<ResponseBody> call = apiStores.downloadFile("sousou.apk"); //note1
        Subscription subscription = call.subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            HttpException response = (HttpException)e;
                            int code = response.code();
                        }
                    }
                    @Override
                    public void onNext(ResponseBody user) {
                        if (DownloadManage.writeResponseBodyToDisk(NetActivity.this, user)) {
                            Toast.makeText(NetActivity.this, "Download is sucess", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        }
    private void getWeather() {
        AppClient.ApiStores apiStores = AppClient.retrofit().create(AppClient.ApiStores.class);
        Call<JsonDemo> call = apiStores.getWeather("json");
        call.enqueue(new Callback<JsonDemo>() {
            @Override
            public void onResponse(Call<JsonDemo> call, Response<JsonDemo> response) {
                Log.i("wxl", "getWeatherinfo=" + response.body().getApiInfo().getApiName());
                try {
                    String string = getCacheDir().toString();
                    FileOutputStream outStream=NetActivity.this.openFileOutput("a.txt", Context.MODE_WORLD_READABLE);
                    String apiinfo = new Gson().toJson(response.body().getApiInfo());
                    outStream.write(apiinfo.getBytes());
                    outStream.close();
                    //Toast.makeText(NetActivity.this,"Saved_"+ string,Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e) {
                    return;
                }
                catch (IOException e){
                    return ;
                }
                setNet(response.body().getApiInfo().getApiName());
            }
            @Override
            public void onFailure(Call<JsonDemo> call, Throwable t) {
                setNet("null");
            }
        });

    }

    public void setNet(String str){
        tv_nethttp.setText(str);
    }
    public class myButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            final ValueAnimator animator = ValueAnimator.ofInt(1, 100);
            animator.setDuration(5000);
            animator.setInterpolator(new BounceInterpolator());//线性效果变化
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    Integer integer = (Integer) animator.getAnimatedValue();
                    tv_nethttp.setText("" + integer);
                    tv_nethttp.setHeight(integer);
                }
            });
            animator.start();
        }
    }
    public class CropSquareTransformation implements Transformation {
        @Override public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }
        @Override public String key() { return "square()"; }
    }
}
