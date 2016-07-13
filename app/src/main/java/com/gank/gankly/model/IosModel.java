package com.gank.gankly.model;

import rx.Subscriber;

/**
 * Create by LingYan on 2016-06-20
 */
public interface IosModel {
    void fetchIos(int page, int limit, Subscriber _s);
}
