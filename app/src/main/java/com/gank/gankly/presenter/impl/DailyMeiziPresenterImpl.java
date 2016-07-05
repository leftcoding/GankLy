package com.gank.gankly.presenter.impl;

import android.app.Activity;

import com.gank.gankly.bean.DailyMeiziBean;
import com.gank.gankly.model.DailyMeiziModel;
import com.gank.gankly.model.impl.DailyMeiziModelImpl;
import com.gank.gankly.presenter.BaseFetchDataPresenter;
import com.gank.gankly.presenter.RefreshPresenter;
import com.gank.gankly.view.IBaseView;
import com.socks.library.KLog;

import java.util.List;

import rx.Subscriber;

/**
 * Create by LingYan on 2016-07-05
 */
public class DailyMeiziPresenterImpl extends BaseFetchDataPresenter implements RefreshPresenter {
    private DailyMeiziModel mModel;

    public DailyMeiziPresenterImpl(Activity mActivity, IBaseView view) {
        super(mActivity, view);
        mModel = new DailyMeiziModelImpl();
    }

    @Override
    public void fetchNew() {
        mModel.fetchDailyMeizi(new Subscriber<List<DailyMeiziBean>>() {
            @Override
            public void onCompleted() {
                KLog.d("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                KLog.e(e);
            }

            @Override
            public void onNext(List<DailyMeiziBean> giftResult) {
                KLog.d("giftResult");
            }
        });
    }

    @Override
    public void fetchMore() {

    }
}
