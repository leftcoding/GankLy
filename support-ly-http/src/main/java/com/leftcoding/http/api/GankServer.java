package com.leftcoding.http.api;

import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.leftcoding.http.BuildConfig;
import com.leftcoding.http.intercept.HttpLogging;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Create by LingYan on 2017-09-29
 * Email:137387869@qq.com
 */

public class GankServer extends BaseServer {
    public static final String GANK_SERVER_URL = BuildConfig.GANK_SERVER_ULR;

    private GankApi mGankApi;

    private GankServer(Context context) {
        final Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .serializeNulls()
                .create();

        final OkHttpClient mOkHttpClient = OkHttpBuilder.get()
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(HttpLogging.get().build())
                .build();

        final Retrofit retrofit = new Retrofit.Builder()
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(GANK_SERVER_URL)
                .build();

        mGankApi = retrofit.create(GankApi.class);

    }

    public static synchronized GankServer with(Context context) {
        return new GankServer(context);
    }

    public GankApi api() {
        return mGankApi;
    }
}
