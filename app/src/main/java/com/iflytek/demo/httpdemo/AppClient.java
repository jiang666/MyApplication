package com.iflytek.demo.httpdemo;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by jianglei on 2017/3/17.
 */

public class AppClient {

    static Retrofit mRetrofit;
    public static Retrofit retrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.1.141/")//http://www.weather.com.cn/
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return mRetrofit;
    }
    public interface ApiStores {

        @GET("{apiName}.html")//adat/sk/{cityId}.html
        Call<JsonDemo> getWeather(@Path("apiName") String apiName);

        @Streaming
        @GET
        Observable<ResponseBody> downloadFile(@Url String   fileUrl);

    }

}
