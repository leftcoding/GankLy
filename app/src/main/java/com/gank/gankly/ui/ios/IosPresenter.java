package com.gank.gankly.ui.ios;

import android.content.Context;
import android.ly.business.api.GankServer;
import android.ly.business.domain.PageConfig;
import android.support.annotation.NonNull;

import com.gank.gankly.mvp.subseribe.PageSubscribe;
import com.leftcoding.rxbus.RxApiManager;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Create by LingYan on 2016-12-20
 */

public class IosPresenter extends IosContract.Presenter {
    private PageConfig pageConfig;
    private final AtomicBoolean destroyFlag = new AtomicBoolean(false);

    public IosPresenter(@NonNull Context context, IosContract.View view) {
        super(context, view);
        pageConfig = new PageConfig();
    }

    @Override
    void refreshIos() {
        pageConfig.curPage = 1;
        fetchData();
    }

    @Override
    void appendIos() {
        fetchData();
    }

    private void fetchData() {
        if (destroyFlag.get()) {
            return;
        }
        GankServer.with(context)
                .ios(pageConfig.curPage, pageConfig.limit)
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
                .subscribe(new PageSubscribe(requestTag, view, pageConfig));
    }

    @Override
    public void destroy() {
        if (destroyFlag.compareAndSet(false, true)) {
            RxApiManager.get().clear(requestTag);
        }
        super.destroy();
    }

    @Override
    protected void onDestroy() {

    }
}
