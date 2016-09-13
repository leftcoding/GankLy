package com.gank.gankly.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.gank.gankly.utils.StyleUtils;
import com.gank.gankly.view.ISwipeRefreshView;

/**
 * Create by LingYan on 2016-09-13
 * Email:137387869@qq.com
 */
public abstract class BaseThemeFragment extends BaseFragment implements ISwipeRefreshView, ISwipeRefresh {
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initPresenter();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void changeThemes() {
        super.changeThemes();
        if (mSwipeRefreshLayout != null) {
            StyleUtils.changeSwipeRefreshLayout(mSwipeRefreshLayout);
        }
    }

    protected abstract void initPresenter();

    @Override
    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        if (swipeRefreshLayout == null) {
            throw new NullPointerException("SwipeRefreshLayout can't be null");
        }
        this.mSwipeRefreshLayout = swipeRefreshLayout;
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
}
