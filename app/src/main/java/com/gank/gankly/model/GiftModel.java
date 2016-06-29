package com.gank.gankly.model;

import com.gank.gankly.bean.GiftResult;
import com.gank.gankly.presenter.OnGiftListener;

import rx.Subscriber;

/**
 * Create by LingYan on 2016-06-29
 */
public interface GiftModel {
    void fetchGift(int page, Subscriber<GiftResult> subscriber);

    void fetchImagesList(String url, OnGiftListener listener);

    int getMaxPageNumber();
}
