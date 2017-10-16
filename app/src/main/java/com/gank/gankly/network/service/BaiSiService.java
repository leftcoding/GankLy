package com.gank.gankly.network.service;

import com.gank.gankly.bean.BuDeJieBean;
import com.gank.gankly.bean.BuDeJieVideo;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import io.reactivex.Observable;

/**
 * Create by LingYan on 2016-11-30
 */

public interface BaiSiService {
    @GET("topic/list/zuixin/10/budejie-android-6.6.1/{limit}-20.json")
    Observable<BuDeJieBean> fetchImage(
            @Path("limit") int nextPage,
            @Query("market") String market,
            @Query("ver") String ver,
            @Query("visiting") String visiting,
            @Query("os") String os,
            @Query("appname") String appname,
            @Query("client") String client,
            @Query("udid") String udid,
            @Query("mac") String mac,
            @Query("model") String model,
            @Query("telcom") String telcom,
            @Query("screenwidth") String screenwidth,
            @Query("screenheight") String screenheight,
            @Query("country") String country
    );

    @GET("topic/list/zuixin/41/budejie-android-6.6.1/{limit}-20.json")
    Observable<BuDeJieVideo> fetchVideo(
            @Path("limit") int nextPage
    );
}
