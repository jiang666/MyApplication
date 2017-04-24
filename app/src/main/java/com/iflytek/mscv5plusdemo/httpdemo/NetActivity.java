package com.iflytek.mscv5plusdemo.httpdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iflytek.mscv5plusdemo.R;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        String internetUrl = "http://i.imgur.com/DvpvklR.png";
        Picasso.with(this)
                .load(internetUrl)
                .into(targetImageView);
        findViewById(R.id.btn_netcall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getWeather();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            //getWeather();
                            /*OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url("http://www.weather.com.cn/adat/sk/101010100.html")
                                    .build();
                            Response response = client.newCall(request).execute();
                            String responseDate = response.body().toString();
                            if(responseDate != null){
                                setNet(responseDate);
                            }else {
                                setNet("null");
                            }*/

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
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
                setNet(response.body().getApiInfo().getApiName());
            }
            @Override
            public void onFailure(Call<JsonDemo> call, Throwable t) {
                setNet("null");
            }
        });
        /*Call<WeatherJson> call = apiStores.getWeather("101010100");
        call.enqueue(new Callback<WeatherJson>() {
            @Override
            public void onResponse(Call<WeatherJson> call, Response<WeatherJson> response) {
                Log.i("wxl", "getWeatherinfo=" + response.body().getWeatherinfo().getCity());
            }
            @Override
            public void onFailure(Call<WeatherJson> call, Throwable t) {
            }
        });*/
    }
    public void setNet(String str){
        tv_nethttp.setText(str);
    }
}
