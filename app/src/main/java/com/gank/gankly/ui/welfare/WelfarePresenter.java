package com.gank.gankly.ui.welfare;

import android.content.Context;
import android.lectcoding.ui.logcat.Logcat;
import android.ly.business.api.GankServer;
import android.ly.business.domain.Gank;
import android.ly.business.domain.PageEntity;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2016-12-23
 */

public class WelfarePresenter extends WelfareContract.Presenter {
    private static final int DEFAULT_LIMIT = 20;

    WelfarePresenter(Context context, WelfareContract.View view) {
        super(context, view);
    }

    @Override
    public void loadWelfare(final int page) {
        showProgress();
        GankServer.with(context)
                .images(page, DEFAULT_LIMIT)
                .subscribe(new Observer<PageEntity<Gank>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PageEntity<Gank> pageEntity) {
                        hideProgress();
                        if (pageEntity != null) {
                            view.loadWelfareSuccess(page, pageEntity.results);
                            return;
                        }
                        if (view != null) {
                            view.loadWelfareFailure("获取数据失败");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgress();
                        Logcat.e(e);
                        if (view != null) {
                            view.loadWelfareFailure("获取数据失败");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onDestroy() {

    }
}
