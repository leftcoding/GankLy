package com.gank.gankly.ui.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

    @NonNull
    private MultipleStatusView mMultipleStatusView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initPresenter();
        super.onViewCreated(view, savedInstanceState);
    }

    protected abstract void initPresenter();

    public void setMultipleStatusView(@NonNull MultipleStatusView multipleStatusView) {
        mMultipleStatusView = multipleStatusView;
    }

    public void showEmpty() {
        showMultipleStatusView(EMPTY);
    }

    public void showContent() {
        showMultipleStatusView(CONTENT);
    }

    public void showDisNetWork() {
        showMultipleStatusView(DIS_NETWORK);
    }

    public void showError() {
        showMultipleStatusView(ERROR);
    }

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
