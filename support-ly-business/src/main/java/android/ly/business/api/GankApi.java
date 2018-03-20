package android.ly.business.api;

import android.ly.business.domain.ListResult;
import android.ly.business.domain.PageEntity;
import android.ly.business.domain.Entity;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;


/**
 * Create by LingYan on 2017-09-29
 */

public interface GankApi {
    @GET("Android/{limit}/{page}")
    Observable<Response<PageEntity<Entity>>> androids(
            @Path("page") int page,
            @Path("limit") int limit
    );

    @GET("iOS/{limit}/{page}")
    Observable<Response<PageEntity<Entity>>> ios(
            @Path("page") int page,
            @Path("limit") int limit

    );

    @GET("all/{limit}/{page}")
    Observable<Response<ListResult<Entity>>> allGoods(
            @Path("page") int page,
            @Path("limit") int limit
    );

    @GET("福利/{limit}/{page}")
    Observable<Response<ListResult<Entity>>> images(
            @Path("page") int page,
            @Path("limit") int limit
    );

    @GET("休息视频/{limit}/{page}")
    Observable<Response<ListResult<Entity>>> videos(
            @Path("page") int page,
            @Path("limit") int limit
    );
}
