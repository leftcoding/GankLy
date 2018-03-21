package com.gank.gankly.ui.android;

import android.content.Context;
import android.ly.business.api.GankServer;
import android.ly.business.domain.Gank;
import android.ly.business.domain.PageEntity;

import com.gank.gankly.mvp.observer.PageOnObserver;
import com.gank.gankly.ui.android.AndroidContract.Presenter;
import com.leftcoding.rxbus.RxApiManager;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Create by LingYan on 2016-10-25
 */
class AndroidPresenter extends Presenter {
    // 请求个数
    private static final int INIT_LIMIT = 20;
    private final AtomicBoolean destroyTag = new AtomicBoolean(false);

    AndroidPresenter(Context context, AndroidContract.View view) {
        super(context, view);
    }

    @Override
    protected void refreshAndroid(int page) {
        fetchAndroid(page);
    }

    @Override
    protected void appendAndroid(int page) {
        fetchAndroid(page);
    }

    @Override
    public void unSubscribe() {
        if (destroyTag.compareAndSet(false, true)) {
            RxApiManager.get().clear(requestTag);
        }
        super.unSubscribe();
    }

    private void fetchAndroid(final int curPage) {
        if (destroyTag.get()) {
            return;
        }

        GankServer.get(context)
                .androids(curPage, INIT_LIMIT)
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
                .subscribe(new PageSubscribe(requestTag, view, isFirst(curPage))
                        .limit(INIT_LIMIT));
    }

    private boolean isFirst(int curPage) {
        return curPage == 1;
    }

    private static class PageSubscribe extends PageOnObserver<PageEntity<Gank>, AndroidContract.View> {
        PageSubscribe(String requestTag, AndroidContract.View view, boolean isFirst) {
            super(requestTag, view, isFirst);
        }

        public PageSubscribe limit(int limit) {
            this.limit = limit;
            return this;
        }

        @Override
        protected void loadMoreSuccess(PageEntity<Gank> gankPageEntity) {
            if (view == null) {
                return;
            }

            if (gankPageEntity.results == null) {
                view.appendAndroidFailure(gankPageEntity.msg);
                return;
            }

            if (gankPageEntity.hasNoMore(limit)) {
                view.hasNoMoreDate();
            }

            view.appendAndroidSuccess(gankPageEntity.results);
        }

        @Override
        protected void loadMoreFailure() {
            if (view != null) {
                view.appendAndroidFailure(null);
            }
        }

        @Override
        protected void onRefreshSuccess(PageEntity<Gank> gankPageEntity) {
            if (view == null) {
                return;
            }

            if (gankPageEntity.results == null) {
                view.refreshAndroidFailure(gankPageEntity.msg);
                view.showEmpty();
                return;
            }

            view.showContent();
            view.refreshAndroidSuccess(gankPageEntity.results);
        }

        @Override
        protected void onRefreshFailure() {
            if (view != null) {
                view.refreshAndroidFailure(null);
                view.showEmpty();
            }
        }
    }
}
