package com.leftcoding.network.base;


import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Create by LingYan on 2017-09-30
 */
public class NetworkControl {
    private static final int DEFAULT_OUT_TIME = 30;
    private OkHttpClient.Builder builder;

    private NetworkControl() {
        builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_OUT_TIME, TimeUnit.SECONDS);
    }

    @SuppressWarnings("unchecked")
    public NetworkControl connectTimeout(long timeout, TimeUnit unit) {
        builder.connectTimeout(timeout, unit);
        return this;
    }

    public NetworkControl addNetworkInterceptor(Interceptor interceptor) {
        builder.addNetworkInterceptor(interceptor);
        return this;
    }

    public NetworkControl addInterceptor(Interceptor interceptor) {
        builder.addInterceptor(interceptor);
        return this;
    }

    public OkHttpClient build() {
        return builder.build();
    }

    private static class SingletonHolder {
        private static final NetworkControl INSTANCE = new NetworkControl();
    }

    static synchronized NetworkControl get() {
        return SingletonHolder.INSTANCE;
    }
}
