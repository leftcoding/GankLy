package com.gank.gankly.mvp.source.remote;

import android.support.annotation.Nullable;

import com.gank.gankly.bean.GankResult;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.mvp.source.BaseDataSourceModel;
import com.gank.gankly.network.api.GankApi;

import rx.Observable;
import rx.functions.Func2;

/**
 * 远程请求数据
 * Create by LingYan on 2016-10-25
 * Email:137387869@qq.com
 */

public class RemoteDataSource extends BaseDataSourceModel {
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

    /**
     * 获取Android 数据
     *
     * @param page  页数
     * @param limit 请求个数
     * @return Observable
     */
    public Observable<GankResult> fetchAndroid(final int page, final int limit) {
        final Observable<GankResult> androidGoods = GankApi.getInstance()
                .getService().fetchAndroidGoods(limit, page);
        Observable<GankResult> images = GankApi.getInstance()
                .getService().fetchBenefitsGoods(limit, page);

        return toObservable(Observable.zip(androidGoods, images, new Func2<GankResult, GankResult, GankResult>() {
            @Override
            public GankResult call(GankResult androidGoods, GankResult images) {
                MeiziArrayList.getInstance().addGiftItems(images.getResults());
                return androidGoods;
            }
        }));
    }



}
