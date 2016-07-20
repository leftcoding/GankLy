package com.gank.gankly.model;

import rx.Subscriber;

/**
 * Create by LingYan on 2016-07-20
 * Email:137387869@qq.com
 */
public interface JiandanModel {
    void fetchData(int page, Subscriber s);
}
