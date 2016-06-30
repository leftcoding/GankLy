package com.gank.gankly.model;

import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.bean.GiftResult;
import com.gank.gankly.view.IGiftView;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Create by LingYan on 2016-06-29
 */
public interface GiftModel {
    void fetchGiftPage(int page, Subscriber<GiftResult> subscriber);

    void fetchImagesPageList(String url, Subscriber<GiftResult> subscription);

    void fetchImagesList(List<GiftBean> list, Subscriber<List<GiftBean>> subscriber, IGiftView iGiftView);

    Subscription getSubscription();

    void setIsUnSubscribe(boolean isUnSubscribe);
}
