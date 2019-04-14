package com.gank.gankly.ui.base;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;


/**
 * Create by LingYan on 2016-04-05
 */
public abstract class BaseSwipeRefreshFragment extends BaseThemeFragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initPresenter();
        super.onViewCreated(view, savedInstanceState);
    }

    protected abstract void initPresenter();
}
