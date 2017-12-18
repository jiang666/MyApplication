package com.iflytek.demo.httpdemo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.iflytek.demo.R;
import com.iflytek.demo.database.DBOpenHelper;
import com.iflytek.demo.util.ACache;
import com.squareup.picasso.Transformation;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
    private ACache aCache;
    private ImageView targetImageView;

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
        targetImageView = (ImageView) findViewById(R.id.vi_nethttp);
        aCache = ACache.get(NetActivity.this);
        /*String internetUrl = "http://192.168.1.145/sousou.png";
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_default)
                .showImageForEmptyUri(R.drawable.ic_default)
                .showImageOnFail(R.drawable.ic_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        ImageLoader.getInstance().displayImage(internetUrl, targetImageView, options);*/
        /*Picasso.with(this)
                .load(internetUrl)
                .transform(new CropSquareTransformation())
                .into(targetImageView);*/
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
            public void onClick(final View v) {
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
                        String path = NetActivity.this.getExternalFilesDir(null) + File.separator + "ooooooo.apk";
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
                        NetActivity.this.startActivity(intent);
                        /*String file = Environment.getExternalStorageState() + File.separator;
                        new FileUtil().deleteFolderFile(file,true);
                        *//*File file6 = new File(file);
                        if(file6.isFile()) {
                            file6.delete();
                        }*/
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
                //EventBus.getDefault().post(new MessageEven(353246));
                /*String string = Environment.getExternalStorageDirectory().getAbsolutePath();
                String path = string + "/a.xml";
                File file = new File(path);
                parser(file);
                setNet("0000");*/
                /*String hh = aCache.getAsString("2222");
                if(hh==null){

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
                }else {
                    setNet(hh);
                }*/

            }
        });
    }

    private void download() {
        Log.e("","");
        AppClient.ApiStores apiStores = AppClient.retrofit().create(AppClient.ApiStores.class);
        //Observable<ResponseBody> call = apiStores.downloadFile("bd.mp4"); //note1
        Observable<ResponseBody> call = apiStores.downloadFile("bt/api/touchsys/program/getMaterial?id=65379c0a051a4a379c0f51ba414ef7c0&cstmId=1d9757c68dbd46c1808b59370ed3d8a1"); //note1
        Subscription subscription = call.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
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
                        String path = Environment.getExternalStorageDirectory().getPath() + "/touch/bd.mp4";
                        try {
                            // todo change the file location/name according to your needs
                            File futureStudioIconFile = new File(path);
                            InputStream inputStream = null;
                            OutputStream outputStream = null;
                            try {
                                byte[] fileReader = new byte[4096];
                                long fileSize = user.contentLength();
                                long fileSizeDownloaded = 0;
                                inputStream = user.byteStream();
                                outputStream = new FileOutputStream(futureStudioIconFile);
                                while (true) {
                                    int read = inputStream.read(fileReader);
                                    if (read == -1) {
                                        break;
                                    }
                                    outputStream.write(fileReader, 0, read);
                                    fileSizeDownloaded += read;
                                    //EventBus.getDefault().post(new MessageEven(fileSizeDownloaded*100/fileSize));
                                    Log.e("========", "file download: " + fileSizeDownloaded + " of " + fileSize);
                                }
                                outputStream.flush();


                            } catch (IOException e) {
                            } finally {
                                if (inputStream != null) {
                                    inputStream.close();
                                }

                                if (outputStream != null) {
                                    outputStream.close();
                                }
                            }
                        } catch (IOException e) {
                        }
                    }
                });
        }
    public void parser(File resFile) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(resFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //获取xmlpull解析器
        XmlPullParser xp = Xml.newPullParser();
        try {
            //初始化
            xp.setInput(is, "utf-8");

            //获取当前节点的事件类型
            int type = xp.getEventType();
            while (type != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        //获取当前节点的名字
                        if ("configs".equals(xp.getName())) {
                        } else if ("program".equals(xp.getName())) {
                            String name = xp.getAttributeValue(null,"name");
                            Log.e("name",name);
                            String programType = xp.getAttributeValue(null,"type");
                            Log.e("type",programType);
                            String version = xp.getAttributeValue(null,"version");
                            Log.e("version",version);

                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("configs".equals(xp.getName())) {
                        }
                        break;

                }
                //把指针移动至下一个节点，并返回该节点的事件类型
                type = xp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    private void getWeather() {
        AppClient.ApiStores apiStores = AppClient.retrofit().create(AppClient.ApiStores.class);
        Call<JsonDemo> call = apiStores.getWeather("json");
        call.enqueue(new Callback<JsonDemo>() {
            @Override
            public void onResponse(Call<JsonDemo> call, Response<JsonDemo> response) {
                Log.i("wxl", "getWeatherinfo=" + response.body().getApiInfo().getApiName());
                try {
                    String string = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String path = string + "/a.xml";
                    Log.e("---path---",path);
                    FileOutputStream outStream = new FileOutputStream(path);
                    String apiinfo = new Gson().toJson(response.body().getApiInfo());
                    try {
                        aCache.put("2222",response.body().getApiInfo().getApiKey());
                    }catch (Exception e){

                    }
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
        /*final int drawableRes = R.drawable.login;
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = getTheme().getDrawable(drawableRes);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        }).subscribe(new Observer<Drawable>() {
            @Override
            public void onNext(Drawable drawable) {
                targetImageView.setImageDrawable(drawable);
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(NetActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });*/

        /*String[] names = {"1","2","3"};
        Observable.from(names)
                .observeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String name) {
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
                        String date=sdf.format(new java.util.Date());
                        Log.d("-------------", name+ date);
                        try {
                            Thread.sleep(20000);
                            Log.d("============", "Observable thread is : " + Thread.currentThread().getName());
                        }catch (Exception e){

                        }

                    }
                });*/
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
