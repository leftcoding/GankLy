package com.gank.gankly.mvp.subseribe;

import android.ly.business.domain.Gank;
import android.ly.business.domain.PageConfig;
import android.ly.business.domain.PageEntity;

import com.gank.gankly.mvp.base.PageView;
import com.gank.gankly.mvp.observer.PageOnObserver;

/**
 * Create by LingYan on 2018-03-22
 */

public class PageSubscribe extends PageOnObserver<PageEntity<Gank>, PageView<Gank>> {
    private PageConfig pageConfig;

    public PageSubscribe(String requestTag, PageView<Gank> view, PageConfig pageConfig) {
        super(requestTag, view);
        this.pageConfig = pageConfig;
    }

    @Override
    protected void loadMoreSuccess(PageEntity<Gank> pageEntity) {
        if (view == null) {
            return;
        }

        if (pageEntity.results == null) {
            view.appendFailure(pageEntity.msg);
            return;
        }

        view.appendSuccess(pageEntity.results);
    }

    @Override
    protected void loadMoreFailure() {
        if (view != null) {
            view.appendFailure(null);
        }
    }

    @Override
    protected boolean isFirst() {
        return pageConfig.isFirst();
    }

    @Override
    protected void onRefreshSuccess(PageEntity<Gank> pageEntity) {
        if (view == null) {
            return;
        }

        if (pageEntity.results == null) {
            view.refreshFailure(pageEntity.msg);
            return;
        }

        view.refreshSuccess(pageEntity.results);
    }

    @Override
    protected void onRefreshFailure() {
        if (view != null) {
            view.refreshFailure(null);
        }
    }

    @Override
    public void onComplete() {
        super.onComplete();
        pageConfig.curPage++;
    }
}