package com.iflytek.mscv5plusdemo.httpdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.iflytek.mscv5plusdemo.R;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jianglei on 2017/3/15.
 */

public class NetActivity extends Activity{
    private TextView tv_nethttp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.net_activity);
        tv_nethttp = (TextView) findViewById(R.id.tv_nethttp);
        findViewById(R.id.btn_netcall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url("http://wwww.baidu.com")
                                    .build();
                            Response response = client.newCall(request).execute();
                            String responseDate = response.body().toString();
                            if(responseDate != null){
                                setNet(responseDate);
                            }else {
                                setNet("null");
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
    public void setNet(String str){
        tv_nethttp.setText(str);
    }
}
