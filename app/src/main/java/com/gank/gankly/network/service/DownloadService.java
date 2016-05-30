package com.gank.gankly.network.service;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import rx.Observable;

/**
 * Create by LingYan on 2016-05-30
 */
public interface DownloadService {
    @Streaming
    @GET("com.ss.android.article.news.apk")
    Observable<ResponseBody> downApk();
}

