package com.gank.gankly.ui.main.tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.gank.gankly.ui.base.fragment.ButterKnifeFragment;

/**
 * Create by LingYan on 2017-09-28
 */

public abstract class MainTabFragment extends ButterKnifeFragment implements MainTabContract.View {
    private MainTabPresenter mPresenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new MainTabPresenter(getContext(), this);
        mPresenter.loadPicture();
    }
}
