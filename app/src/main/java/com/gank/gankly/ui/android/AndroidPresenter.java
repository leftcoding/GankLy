package com.gank.gankly.ui.android;

import android.content.Context;
import android.ly.business.api.GankServer;
import android.ly.business.domain.PageConfig;

import com.gank.gankly.ui.android.AndroidContract.Presenter;
import com.gank.gankly.mvp.subseribe.PageSubscribe;
import com.leftcoding.rxbus.RxApiManager;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Create by LingYan on 2016-10-25
 */
class AndroidPresenter extends Presenter {
    // 请求个数
    private static final int INIT_LIMIT = 20;
    private final AtomicBoolean destroyTag = new AtomicBoolean(false);
    private PageConfig pageConfig;

    AndroidPresenter(Context context, AndroidContract.View view) {
        super(context, view);
        pageConfig = new PageConfig();
    }

    @Override
    protected void refreshAndroid() {
        pageConfig.curPage = 1;
        fetchAndroid();
    }

    @Override
    protected void appendAndroid() {
        fetchAndroid();
    }

    @Override
    public void destroy() {
        if (destroyTag.compareAndSet(false, true)) {
            RxApiManager.get().clear(requestTag);
        }
        super.destroy();
    }

    @Override
    protected void onDestroy() {

    }

    private void fetchAndroid() {
        if (destroyTag.get()) {
            return;
        }

        GankServer.with(context)
                .androids(pageConfig.curPage, INIT_LIMIT)
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
}
