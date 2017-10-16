package com.leftcoding.http.api;


import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Create by LingYan on 2017-09-30
 * Email:137387869@qq.com
 */
public class OkHttpBuilder {
    private static final int DEFAULT_OUT_TIME = 30;
    private OkHttpClient.Builder builder;

    private OkHttpBuilder() {
        builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_OUT_TIME, TimeUnit.SECONDS);
    }

    @SuppressWarnings("unchecked")
    public OkHttpBuilder connectTimeout(long timeout, TimeUnit unit) {
        builder.connectTimeout(timeout, unit);
        return this;
    }

    OkHttpBuilder addNetworkInterceptor(Interceptor interceptor) {
        builder.addNetworkInterceptor(interceptor);
        return this;
    }

    OkHttpBuilder addInterceptor(Interceptor interceptor) {
        builder.addInterceptor(interceptor);
        return this;
    }

    OkHttpClient build() {
        return builder.build();
    }

    private static class SingletonHolder {
        private static final OkHttpBuilder INSTANCE = new OkHttpBuilder();
    }

    static synchronized OkHttpBuilder get() {
        return SingletonHolder.INSTANCE;
    }
}
