package com.gank.gankly.ui.main.tab;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

import com.gank.gankly.butterknife.ButterKnifeFragment;

/**
 * Create by LingYan on 2017-09-28
 */

public abstract class MainTabFragment extends ButterKnifeFragment implements MainTabContract.View {
    private MainTabPresenter mPresenter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new MainTabPresenter(getContext(), this);
        mPresenter.loadPicture();
    }
}
