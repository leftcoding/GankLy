package com.gank.gankly.ui.base;

import com.gank.gankly.ui.base.activity.BaseActivity;
import com.gank.gankly.view.ISwipeRefreshView;

/**
 * Create by LingYan on 2016-07-20
 */
public abstract class BaseJiandanActivity extends BaseActivity implements ISwipeRefreshView {
    @Override
    protected int getContentId() {
        return 0;
    }

    @Override
    public void hideRefresh() {

    }

    @Override
    public void showRefresh() {

    }

    @Override
    public void hasNoMoreDate() {

    }

    @Override
    public void clear() {

    }

    @Override
    public void showRefreshError(String errorStr) {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showDisNetWork() {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void showLoading() {

    }
}
