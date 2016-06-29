package com.gank.gankly.model.impl;

import com.gank.gankly.bean.GankResult;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.model.IosModel;
import com.gank.gankly.network.api.GankApi;
import com.gank.gankly.presenter.OnFetchListener;

import rx.Subscriber;

/**
 * Create by LingYan on 2016-06-20
 */
public class IosModelImpl implements IosModel {
    private OnFetchListener<ResultsBean> mListener;

    public IosModelImpl(OnFetchListener<ResultsBean> listener) {
        this.mListener = listener;
    }

    @Override
    public void fetchIos(final int page, int limit) {
        Subscriber<GankResult> subscriber = new Subscriber<GankResult>() {
            @Override
            public void onCompleted() {
                mListener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                mListener.onError(e);
            }

            @Override
            public void onNext(GankResult gankResult) {
                boolean isEmpty = true;
                if (gankResult != null) {
                    if (gankResult.getSize() != 0) {
                        isEmpty = false;
                    }
                }

                if (isEmpty) {
                    mListener.onEmpty();
                } else {
                    mListener.onNext(gankResult.getResults());
                }
            }
        };

        GankApi.getInstance().fetchIos(limit, page, subscriber);
    }
}
