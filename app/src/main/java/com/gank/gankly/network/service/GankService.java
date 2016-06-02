package com.gank.gankly.network.service;

import com.gank.gankly.bean.GankResult;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Create by LingYan on 2016-04-06
 */
public interface GankService {
    @GET("Android/{limit}/{page}")
    Observable<GankResult> fetchAndroidGoods(
            @Path("limit") int limit,
            @Path("page") int page
    );

    @GET("iOS/{limit}/{page}")
    Observable<GankResult> fetchIosGoods(
            @Path("limit") int limit,
            @Path("page") int page
    );

    @GET("all/{limit}/{page}")
    Observable<GankResult> fetchAllGoods(
            @Path("limit") int limit,
            @Path("page") int page
    );

    @GET("福利/{limit}/{page}")
    Observable<GankResult> fetchBenefitsGoods(
            @Path("limit") int limit,
            @Path("page") int page
    );

    @GET("休息视频/{limit}/{page}")
    Observable<GankResult> fetchVideo(
            @Path("limit") int limit,
            @Path("page") int page
    );
}
