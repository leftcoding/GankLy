package com.gank.gankly.model;

import com.gank.gankly.bean.DailyMeiziBean;
import com.gank.gankly.bean.GiftBean;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Create by LingYan on 2016-07-05
 */
public interface DailyMeiziModel {
    void fetchDailyMeizi(Subscriber<List<DailyMeiziBean>> subscriber);

    void fetchImageUrls(String url, Subscriber<List<GiftBean>> subscriber);

    void fetchImageList(List<GiftBean> list, Action1<Integer> action1, Subscriber<List<GiftBean>> _subscriber);

    Subscription getSubscription();

    void setUnSubscribe(boolean unSubscribe);

    boolean getUnSubscribe();
}
