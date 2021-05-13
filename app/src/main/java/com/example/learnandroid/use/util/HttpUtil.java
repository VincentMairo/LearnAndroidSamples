package com.example.learnandroid.use.util;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpUtil {
    public static final String TAG = HttpUtil.class.getSimpleName();

    public static String BASE_URL = "https://www.wanandroid.com";

    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }

    public static Retrofit getOnlineCookieRetrofit() {
        //请求用okhttp，响应用rxjava

        //okthhp 客户端
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = builder
                .addNetworkInterceptor(new StethoInterceptor())
                .readTimeout(10000, TimeUnit.SECONDS)
                .writeTimeout(10000, TimeUnit.SECONDS)
                .connectTimeout(10000, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                //网络请求框架
                .client(okHttpClient)
                //json解析
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                //rxjava处理工具
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
