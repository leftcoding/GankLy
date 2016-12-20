package com.gank.gankly.network.api;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Create by LingYan on 2016-12-20
 * Email:137387869@qq.com
 */

public class ApiManager {
    private static final int DEFAULT_OUT_TIME = 30;
    private static ApiManager sApiManager;

    private final static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .serializeNulls()
            .create();

    private Retrofit retrofit;

    public static ApiManager init(String url) {
        if (sApiManager == null) {
            synchronized (ApiManager.class) {
                if (sApiManager == null) {
                    sApiManager = new ApiManager(url);
                }
            }
        }
        return sApiManager;
    }


    public ApiManager(String url) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_OUT_TIME, TimeUnit.SECONDS); //手动创建一个OkHttpClient并设置超时时间
        builder.addNetworkInterceptor(new StethoInterceptor()); //chrome test databases
//        builder.interceptors().add(new LoggingInterceptor()); //打印请求log
//        builder.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Interceptor.Chain chain) throws IOException {
//                Request original = chain.request();
//
//                Request request = original.newBuilder()
//                        .header("Content-Type", "application/json")
//                        .header("Cache-Control", "public, max-age=" + 60 * 60 * 4)
//                        .build();
//                KLog.d(chain.proceed(request).body().string());
//                return chain.proceed(request);
//            }
//        });

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(url)
                .build();
    }

    public <T> T createService(final Class<T> clz) {
        return retrofit.create(clz);
    }
}
