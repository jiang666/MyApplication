package com.iflytek.mscv5plusdemo.httpdemo;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by jianglei on 2017/3/17.
 */

public class AppClient {

    static Retrofit mRetrofit;
    public static Retrofit retrofit() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.43.81/")//http://www.weather.com.cn/
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit;
    }
    public interface ApiStores {
        @GET("{apiName}.html")//adat/sk/{cityId}.html
        Call<JsonDemo> getWeather(@Path("apiName") String apiName);
    }
}
