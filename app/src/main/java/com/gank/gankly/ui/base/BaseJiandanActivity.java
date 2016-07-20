package com.gank.gankly.ui.base;

import com.gank.gankly.view.ISwipeRefreshView;

/**
 * Create by LingYan on 2016-07-20
 * Email:137387869@qq.com
 */
public abstract class BaseJiandanActivity extends BaseActivity implements ISwipeRefreshView {
    @Override
    protected int getContentId() {
        return 0;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void initValues() {

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
