package com.gank.gankly.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.gank.gankly.widget.MultipleStatusView;


/**
 * Create by LingYan on 2016-04-05
 * Email:137387869@qq.com
 */
public abstract class BaseSwipeRefreshFragment extends BaseThemeFragment {
    private static final int LOADING = 1;
    private static final int EMPTY = 2;
    private static final int CONTENT = 3;
    private static final int ERROR = 4;
    private static final int DIS_NETWORK = 5;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initPresenter();
        super.onViewCreated(view, savedInstanceState);
    }

    protected abstract void initPresenter();

    private MultipleStatusView mMultipleStatusView;

    public void setMultipleStatusView(MultipleStatusView multipleStatusView) {
        mMultipleStatusView = multipleStatusView;
        checkMultiple();
    }

    private void checkMultiple() {
        if (mMultipleStatusView == null) {
            throw new NullPointerException("MultipleStatusView can't be null");
        }
    }

    @Override
    public void showEmpty() {
        showMultipleStatusView(EMPTY);
    }

    @Override
    public void showContent() {
        showMultipleStatusView(CONTENT);
    }

    @Override
    public void showDisNetWork() {
        showMultipleStatusView(DIS_NETWORK);
    }

    @Override
    public void showError() {
        showMultipleStatusView(ERROR);
    }

    @Override
    public void showLoading() {
        showMultipleStatusView(LOADING);
    }

    private void showMultipleStatusView(int status) {
        if (mMultipleStatusView == null) {
            return;
        }
        switch (status) {
            case 1:
                mMultipleStatusView.showLoading();
                break;
            case 2:
                mMultipleStatusView.showEmpty();
                break;
            case 3:
                mMultipleStatusView.showContent();
                break;
            case 4:
                mMultipleStatusView.showError();
                break;
            case 5:
                mMultipleStatusView.showNoNetwork();
                break;
            default:
                break;
        }
    }
}
