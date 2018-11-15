package com.gank.gankly.ui.base.activity;

import com.gank.gankly.mvp.base.SupportView;

/**
 * Create by LingYan on 2017-10-03
 */

public abstract class SupportActivity extends BaseActivity implements SupportView {
    @Override
    public void hasNoMoreDate() {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void shortToast(String msg) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showDisNetWork() {

    }

    @Override
    public void showError() {

    }

    @Override
    protected int getContentId() {
        return 0;
    }
}
