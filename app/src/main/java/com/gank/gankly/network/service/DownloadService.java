package com.gank.gankly.network.service;

<<<<<<< HEAD
import com.gank.gankly.bean.CheckVersion;

=======
>>>>>>> ba456322f78a58bea1ad2febbfee870809b766d1
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import rx.Observable;

/**
 * Create by LingYan on 2016-05-30
 */
public interface DownloadService {
    @Streaming
<<<<<<< HEAD
    @GET("gankly.apk")
    Observable<ResponseBody> downloadApk();

    @GET("CheckVersion.json")
    Observable<CheckVersion> checkVersion();
=======
    @GET("168licai.apk")
    Observable<ResponseBody> downApk();
>>>>>>> ba456322f78a58bea1ad2febbfee870809b766d1
}

