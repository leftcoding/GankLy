package com.gank.gankly.presenter;

import java.util.List;

/**
 * Create by LingYan on 2016-06-20
 */
public interface onFetchListener<P> {
    void onCompleted();

    void onError(Throwable e);

    void onNext(List<P> list);

    void onEmpty();
}
