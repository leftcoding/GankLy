package com.gank.gankly.model.impl;

import com.gank.gankly.model.BaseModel;
import com.gank.gankly.network.api.GankApi;

import rx.Subscriber;

/**
 * Create by LingYan on 2016-07-13
 * Email:137387869@qq.com
 */
public class MeiziModelImpl implements BaseModel {
    public MeiziModelImpl() {

    }

    @Override
    public void fetchDate(int page, int limit, Subscriber subscriber) {
        GankApi.getInstance().fetchWelfare(limit, page, subscriber);
    }
}
