package android.ly.business.api;

import android.ly.business.domain.ListEntity;
import android.ly.business.domain.PageEntity;
import android.ly.business.domain.Gank;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;


/**
 * Create by LingYan on 2017-09-29
 */

public interface GankApi {
    /**
     * android 资源
     */
    @GET("Android/{limit}/{page}")
    Observable<Response<PageEntity<Gank>>> androids(
            @Path("page") int page,
            @Path("limit") int limit
    );

    /**
     * ios 资源
     */
    @GET("iOS/{limit}/{page}")
    Observable<Response<PageEntity<Gank>>> ios(
            @Path("page") int page,
            @Path("limit") int limit

    );

    @GET("all/{limit}/{page}")
    Observable<Response<ListEntity<Gank>>> allGoods(
            @Path("page") int page,
            @Path("limit") int limit
    );

    /**
     * 福利 - 图片
     */
    @GET("福利/{limit}/{page}")
    Observable<Response<PageEntity<Gank>>> images(
            @Path("page") int page,
            @Path("limit") int limit
    );

    @GET("休息视频/{limit}/{page}")
    Observable<Response<ListEntity<Gank>>> videos(
            @Path("page") int page,
            @Path("limit") int limit
    );
}
