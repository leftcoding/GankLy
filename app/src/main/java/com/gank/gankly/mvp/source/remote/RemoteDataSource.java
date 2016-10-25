package com.gank.gankly.mvp.source.remote;

import android.support.annotation.Nullable;

import com.gank.gankly.bean.GankResult;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.mvp.BaseModel;
import com.gank.gankly.network.api.GankApi;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Create by LingYan on 2016-10-25
 * Email:137387869@qq.com
 */

public class RemoteDataSource extends BaseModel {
    @Nullable
    private static RemoteDataSource INSTANCE = null;

    public static RemoteDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (RemoteDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RemoteDataSource();
                }
            }
        }
        return INSTANCE;
    }

    public Observable<GankResult> fetchAndroid(final int page, final int limit) {
        final Observable<GankResult> androidGoods = GankApi.getInstance()
                .getGankService().fetchAndroidGoods(limit, page);
        Observable<GankResult> images = GankApi.getInstance()
                .getGankService().fetchBenefitsGoods(limit, page);

        return Observable.zip(androidGoods, images, new Func2<GankResult, GankResult, GankResult>() {
            @Override
            public GankResult call(GankResult androidGoods, GankResult images) {
                MeiziArrayList.getInstance().addGiftItems(images.getResults());
                return androidGoods;
            }
        })
                .retry(3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
