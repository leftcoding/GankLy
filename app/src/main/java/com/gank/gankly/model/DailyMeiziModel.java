package com.gank.gankly.model;

import com.gank.gankly.bean.DailyMeiziBean;

import java.util.List;

import rx.Subscriber;

/**
 * Create by LingYan on 2016-07-05
 */
public interface DailyMeiziModel {
    void fetchDailyMeizi(Subscriber<List<DailyMeiziBean>> subscriber);
}
