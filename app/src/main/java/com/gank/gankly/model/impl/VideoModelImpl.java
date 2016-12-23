package com.gank.gankly.model.impl;

import com.gank.gankly.bean.GankResult;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.model.BaseModel;
import com.gank.gankly.network.api.GankApi;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by LingYan on 2016-07-14
 * Email:137387869@qq.com
 */
public class VideoModelImpl implements BaseModel {
    public VideoModelImpl() {

    }

    @Override
    public void fetchData(final int mPage, int limit, Observer subscriber) {
        Observable<GankResult> video = GankApi.getInstance()
                .getService().fetchVideo(limit, mPage);
        Observable<GankResult> image = GankApi.getInstance()
                .getService().fetchBenefitsGoods(limit, mPage);
        Observable.zip(video, image, new BiFunction<GankResult, GankResult, GankResult>() {
            @Override
            public GankResult apply(GankResult gankResult, GankResult gankResult2) throws Exception {
                addImages(gankResult2, mPage);
                return gankResult;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    private void addImages(GankResult gankResult, int mPage) {
        if (gankResult != null) {
            int page = MeiziArrayList.getInstance().getPage();
            if (page == 0 || page < mPage) {
                MeiziArrayList.getInstance().addBeanAndPage(gankResult.getResults(), mPage);
            }
        }
    }
}
