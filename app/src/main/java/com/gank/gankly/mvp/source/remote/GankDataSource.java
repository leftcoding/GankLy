package com.gank.gankly.mvp.source.remote;

import android.support.annotation.Nullable;

import com.gank.gankly.bean.GankResult;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.mvp.source.BaseDataSourceModel;
import com.gank.gankly.network.api.ApiManager;
import com.gank.gankly.network.service.GankService;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;

/**
 * 干货远程请求
 * Create by LingYan on 2016-10-25
 * Email:137387869@qq.com
 */

public class GankDataSource extends BaseDataSourceModel {
    private static final String BASE_URL = "http://gank.io/api/data/";
    private GankService mGankService;

    @Nullable
    private static GankDataSource INSTANCE;

    private GankDataSource() {
        mGankService = ApiManager.init(BASE_URL).createService(GankService.class);
    }

    public static GankDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (GankDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GankDataSource();
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
        final Observable<GankResult> androidGoods = mGankService.fetchAndroidGoods(limit, page);
        final Observable<GankResult> images = mGankService.fetchBenefitsGoods(limit, page);

        return toObservable(Observable.zip(androidGoods, images, new BiFunction<GankResult, GankResult, GankResult>() {
            @Override
            public GankResult apply(GankResult androidGoods, GankResult images) throws Exception {
                MeiziArrayList.getInstance().addGiftItems(images.getResults());
                return androidGoods;
            }
        }));
    }

    public Observable<GankResult> fetchIos(final int page, final int limit) {
        return toObservable(mGankService.fetchIosGoods(limit, page));
    }

    public Observable<GankResult> fetchBenefitsGoods(final int page, final int limit) {
        return toObservable(mGankService.fetchBenefitsGoods(limit, page));
    }
}
