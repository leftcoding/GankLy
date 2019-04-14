package com.gank.gankly.ui.android;

import android.content.Context;
import android.ly.business.api.GankServer;
import android.ly.business.domain.Gank;
import android.ly.business.domain.PageEntity;

import com.gank.gankly.mvp.observer.RefreshOnObserver;
import com.gank.gankly.ui.android.AndroidContract.Presenter;
import com.leftcoding.rxbus.RxApiManager;

/**
 * Create by LingYan on 2016-10-25
 */
class AndroidPresenter extends Presenter {
    // 请求个数
    private static final int INIT_LIMIT = 20;

    AndroidPresenter(Context context, AndroidContract.View view) {
        super(context, view);
    }

    @Override
    public void loadAndroid(int page) {
        if (isDestroy()) {
            return;
        }

        GankServer.with(context)
                .androids(page, INIT_LIMIT)
                .doOnSubscribe(disposable -> {
                    if (isViewLife()) {
                        view.showProgress();
                    }
                })
                .doFinally(() -> {
                    if (isViewLife()) {
                        view.hideProgress();
                    }
                })
                .subscribe(new RefreshOnObserver<PageEntity<Gank>>(requestTag) {
                    @Override
                    protected void onSuccess(PageEntity<Gank> entity) {
                        if (isViewLife()) {
                            if (entity != null) {
                                view.refreshSuccess(entity.results);
                                return;
                            }
                            view.refreshFailure("");
                        }
                    }

                    @Override
                    protected void onFailure() {
                        if (isViewLife()) {
                            view.refreshFailure("");
                        }
                    }
                });
    }

    @Override
    public void destroy() {
        super.destroy();
        RxApiManager.get().clear(requestTag);
    }

    @Override
    protected void onDestroy() {

    }

    private void fetchAndroid() {


    }
}
