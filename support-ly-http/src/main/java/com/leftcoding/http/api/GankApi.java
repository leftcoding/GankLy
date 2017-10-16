package com.leftcoding.http.api;

import com.leftcoding.http.bean.ListResult;
import com.leftcoding.http.bean.PageResult;
import com.leftcoding.http.bean.ResultsBean;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Create by LingYan on 2017-09-29
 * Email:137387869@qq.com
 */

public interface GankApi {
    @GET("Android/{limit}/{page}")
    Observable<Response<PageResult<ResultsBean>>> androids(
            @Path("page") int page,
            @Path("limit") int limit
    );

    @GET("iOS/{limit}/{page}")
    Observable<Response<PageResult<ResultsBean>>> ios(
            @Path("page") int page,
            @Path("limit") int limit

    );

    @GET("all/{limit}/{page}")
    Observable<Response<ListResult<ResultsBean>>> allGoods(
            @Path("page") int page,
            @Path("limit") int limit
    );

    @GET("福利/{limit}/{page}")
    Observable<Response<ListResult<ResultsBean>>> images(
            @Path("page") int page,
            @Path("limit") int limit
    );

    @GET("休息视频/{limit}/{page}")
    Observable<Response<ListResult<ResultsBean>>> videos(
            @Path("page") int page,
            @Path("limit") int limit
    );
}
