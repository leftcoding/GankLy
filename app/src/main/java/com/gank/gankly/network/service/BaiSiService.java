package com.gank.gankly.network.service;

import com.gank.gankly.bean.BaiSiBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Create by LingYan on 2016-11-30
 * Email:137387869@qq.com
 */

public interface BaiSiService {
    @FormUrlEncoded
    @POST("255-1")
    Observable<BaiSiBean> fetchBaiSi(
            @Field("showapi_appid") String appid,
            @Field("showapi_sign") String sign,
            @Field("showapi_timestamp") String timestamp,
            @Field("showapi_sign_method") String method,
            @Field("showapi_res_gzip") String gzip,
            @Field("type") String type,
            @Field("title") String title,
            @Field("page") int page
    );
}
