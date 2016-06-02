package com.gank.gankly.network.api;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.gank.gankly.network.service.DownloadService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Create by LingYan on 2016-05-30
 */
public class DownloadApi {
    private static final int DEFAULT_OUT_TIME = 12;
    private static final String BASE_URL = "";
    private DownloadService mDownloadService;

    private DownloadApi() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_OUT_TIME, TimeUnit.SECONDS); //手动创建一个OkHttpClient并设置超时时间
        builder.addNetworkInterceptor(new StethoInterceptor()); //chrome test databases

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        mDownloadService = retrofit.create(DownloadService.class);
    }
}
