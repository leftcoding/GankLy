package com.gank.gankly.model.impl;

import com.gank.gankly.model.JiandanModel;
import com.gank.gankly.network.api.JiandanApi;

import rx.Subscriber;

/**
 * Create by LingYan on 2016-07-20
 * Email:137387869@qq.com
 */
public class JiandanModelImpl implements JiandanModel {
    public JiandanModelImpl() {

    }

    @Override
    public void fetchData(int page, Subscriber subscriber) {
        JiandanApi.getInstance().fetchJianDan(page, subscriber);
    }
}
