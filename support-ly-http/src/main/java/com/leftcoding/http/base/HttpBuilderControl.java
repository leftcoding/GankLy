package com.leftcoding.http.base;


import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Create by LingYan on 2017-09-30
 */
public class HttpBuilderControl {
    private static final int DEFAULT_OUT_TIME = 30;
    private OkHttpClient.Builder builder;

    private HttpBuilderControl() {
        builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_OUT_TIME, TimeUnit.SECONDS);
    }

    @SuppressWarnings("unchecked")
    public HttpBuilderControl connectTimeout(long timeout, TimeUnit unit) {
        builder.connectTimeout(timeout, unit);
        return this;
    }

    public HttpBuilderControl addNetworkInterceptor(Interceptor interceptor) {
        builder.addNetworkInterceptor(interceptor);
        return this;
    }

    public HttpBuilderControl addInterceptor(Interceptor interceptor) {
        builder.addInterceptor(interceptor);
        return this;
    }

    public OkHttpClient build() {
        return builder.build();
    }

    private static class SingletonHolder {
        private static final HttpBuilderControl INSTANCE = new HttpBuilderControl();
    }

    static synchronized HttpBuilderControl get() {
        return SingletonHolder.INSTANCE;
    }
}
