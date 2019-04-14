package com.gank.gankly.ui.base.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.gank.gankly.butterknife.ButterKnifeFragment;
import com.gank.gankly.mvp.base.SupportView;
import com.gank.gankly.utils.ToastUtils;

/**
 * Create by LingYan on 2017-09-28
 */

public abstract class SupportFragment extends ButterKnifeFragment implements SupportView {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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
    public void shortToast(String str) {
        if (!TextUtils.isEmpty(str)) {
            ToastUtils.showToast(getContext(), str);
        }
    }
}
