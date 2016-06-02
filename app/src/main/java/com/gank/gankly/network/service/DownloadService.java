package com.gank.gankly.network.service;

import com.gank.gankly.bean.CheckVersion;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import rx.Observable;

/**
 * Create by LingYan on 2016-05-30
 */
public interface DownloadService {
    @Streaming
    @GET("gankly.apk")
    Observable<ResponseBody> downloadApk();

    @GET("CheckVersion.json")
    Observable<CheckVersion> checkVersion();
}

