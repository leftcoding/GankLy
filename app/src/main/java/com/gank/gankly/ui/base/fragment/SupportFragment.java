package com.gank.gankly.ui.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.gank.gankly.mvp.base.SupportView;
import com.gank.gankly.utils.ToastUtils;

/**
 * Create by LingYan on 2017-09-28
 */

public abstract class SupportFragment extends ButterKnifeFragment implements SupportView {
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void hasNoMoreDate() {

    }

    @Override
    public void showContent() {

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
    public void showLoading() {

    }

    @Override
    public void showShortToast(String str) {
        if(!TextUtils.isEmpty(str)){
            ToastUtils.showToast(str);
        }
    }

    @Override
    public void showRefreshError(String errorStr) {

    }
}
