package com.gank.gankly.mvp.source.remote;

import com.gank.gankly.bean.GankResult;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.network.api.ApiManager;
import com.gank.gankly.network.service.GankService;

import io.reactivex.Observable;

/**
 * 干货远程请求
 * Create by LingYan on 2016-10-25
 */

public class GankDataSource {
    private static final String BASE_URL = "http://gank.io/api/data/";
    private GankService mGankService;

    private volatile static GankDataSource INSTANCE;

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
    public Observable<GankResult> fetchAndroidAndImages(final int page, final int limit) {
        final Observable<GankResult> androidGoods = mGankService.fetchAndroid(limit, page);
        final Observable<GankResult> images = mGankService.fetchImages(limit, page);

        return Observable.zip(androidGoods, images, (androidGoods1, images1) -> {
            MeiziArrayList.getInstance().refillOneItems(images1.getResults());
            return androidGoods1;
        });
    }

    public Observable<GankResult> fetchAndroid(final int page, final int limit) {
        return mGankService.fetchAndroid(limit, page);
    }


    public Observable<GankResult> fetchIos(final int page, final int limit) {
        return mGankService.fetchIosGoods(limit, page);
    }

    /**
     * 干货图片
     */
    public Observable<GankResult> fetchWelfare(final int page, final int limit) {
        return mGankService.fetchImages(limit, page);
    }

    /**
     * 视频
     */
    public Observable<GankResult> fetchVideo(final int page, final int limit) {
        return mGankService.fetchVideo(limit, page);
    }

    /**
     * 视频和图片
     */
    public Observable<GankResult> fetchVideoAndImages(final int page, final int limit) {
        final Observable<GankResult> androidGoods = mGankService.fetchVideo(limit, page);
        final Observable<GankResult> images = mGankService.fetchImages(limit, page);

        return Observable.zip(androidGoods, images, (androidGoods1, images1) -> {
            MeiziArrayList.getInstance().refillOneItems(images1.getResults());
            return androidGoods1;
        });
    }
}
