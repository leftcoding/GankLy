package com.leftcoding.http.api;

import android.content.Context;

import com.leftcoding.http.bean.ListResult;
import com.leftcoding.http.bean.PageResult;
import com.leftcoding.http.bean.ResultsBean;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Create by LingYan on 2017-09-30
 */

public class GankManager {
    private GankApi mApi;

    private GankManager(Context context) {
        mApi = GankServer.with(context).api();
    }

    public static synchronized GankManager with(Context context) {
        return new GankManager(context);
    }

    public Observable<Response<PageResult<ResultsBean>>> androids(int page, int limit) {
        return mApi.androids(page, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Response<PageResult<ResultsBean>>> ios(int page, int limit) {
        return mApi.ios(page, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Response<ListResult<ResultsBean>>> allGoods(int limit, int page) {
        return mApi.allGoods(limit, page);
    }

    public Observable<Response<ListResult<ResultsBean>>> images(int limit, int page) {
        return mApi.images(limit, page);
    }

    public Observable<Response<ListResult<ResultsBean>>> videos(int limit, int page) {
        return mApi.videos(limit, page);
    }
}
