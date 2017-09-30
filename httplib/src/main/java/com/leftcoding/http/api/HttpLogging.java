package com.leftcoding.http.api;

import android.support.annotation.NonNull;

import com.socks.library.KLog;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Create by LingYan on 2017-09-30
 * Email:137387869@qq.com
 */

public class HttpLogging {
    private HttpLoggingInterceptor logging;

    private HttpLogging() {
        logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NonNull String message) {
                KLog.d(message);
            }
        });

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    public HttpLoggingInterceptor build() {
        return logging;
    }

    private static class SingletonHolder {
        private static final HttpLogging INSTANCE = new HttpLogging();
    }

    public static synchronized HttpLogging get() {
        return SingletonHolder.INSTANCE;
    }
}
