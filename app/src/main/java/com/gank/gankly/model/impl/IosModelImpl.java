package com.gank.gankly.model.impl;

import com.gank.gankly.model.IosModel;
import com.gank.gankly.network.api.GankApi;

import rx.Subscriber;

/**
 * Create by LingYan on 2016-06-20
 */
public class IosModelImpl implements IosModel {
    public IosModelImpl() {
    }

    @Override
    public void fetchIos(final int page, int limit, Subscriber _subscriber) {

        GankApi.getInstance().fetchIos(limit, page, _subscriber);
    }

}
