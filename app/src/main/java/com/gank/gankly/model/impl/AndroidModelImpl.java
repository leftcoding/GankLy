package com.gank.gankly.model.impl;

import com.gank.gankly.model.BaseModel;
import com.gank.gankly.network.api.GankApi;

import rx.Subscriber;

/**
 * Create by LingYan on 2016-07-20
 * Email:137387869@qq.com
 */
public class AndroidModelImpl implements BaseModel {

    public AndroidModelImpl() {

    }

    @Override
    public void fetchData(int page, int limit, Subscriber subscriber) {
        GankApi.getInstance().fetchAndroid(limit, page, subscriber);
    }
}
