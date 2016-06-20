package com.gank.gankly.presenter;

import android.app.Activity;

import com.gank.gankly.view.IBaseView;

import java.util.List;

/**
 * Create by LingYan on 2016-06-20
 */
public class BaseFetchDataPresenter<E extends IBaseView, P> extends BasePresenter<E> implements
        onFetchListener<P> {

    public BaseFetchDataPresenter(Activity mActivity, E view) {
        super(mActivity, view);
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(List<P> list) {

    }

    @Override
    public void onEmpty() {

    }
}
