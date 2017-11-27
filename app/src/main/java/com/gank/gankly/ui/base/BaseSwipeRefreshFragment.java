package com.gank.gankly.ui.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;


/**
 * Create by LingYan on 2016-04-05
 * Email:137387869@qq.com
 */
public abstract class BaseSwipeRefreshFragment extends BaseThemeFragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initPresenter();
        super.onViewCreated(view, savedInstanceState);
    }

    protected abstract void initPresenter();
}
