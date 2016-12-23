package com.gank.gankly.network.service;

import com.gank.gankly.bean.JiandanResult;

import retrofit2.http.GET;
import retrofit2.http.Query;
import io.reactivex.Observable;

/**
 * Create by LingYan on 2016-07-20
 * Email:137387869@qq.com
 */
public interface JiandanService {
    @GET("/")
    Observable<JiandanResult> fetchData(
            @Query("oxwlxojflwblxbsapi") String oxw, @Query("include") String includes,
            @Query("custom_fields") String custom, @Query("dev") String dev, @Query("page") String page
    );
}
