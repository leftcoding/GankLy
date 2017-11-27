package com.leftcoding.http.intercept;

import android.support.annotation.NonNull;
import android.util.Log;

import com.leftcoding.http.BuildConfig;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Create by LingYan on 2017-09-30
 */

public class HttpLogging {
    private static final String TAG = "OkHttpLogging";
    private HttpLoggingInterceptor mLogging;
    private HttpLoggingInterceptor.Level mLevel;

    private HttpLogging() {
        mLogging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NonNull String message) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, message);
                }
            }
        });

        mLevel = HttpLoggingInterceptor.Level.NONE;
    }

    public HttpLogging setLevel(HttpLoggingInterceptor.Level level) {
        mLevel = level;
        return this;
    }

    public HttpLoggingInterceptor build() {
        return mLogging.setLevel(mLevel);
    }

    private static class SingletonHolder {
        private static final HttpLogging INSTANCE = new HttpLogging();
    }

    public static synchronized HttpLogging get() {
        return SingletonHolder.INSTANCE;
    }
}
