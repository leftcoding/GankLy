package com.gank.gankly.mvp.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Create by LingYan on 2016-10-21
 * Email:137387869@qq.com
 */

public abstract class FetchFragment extends ThemeFragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initPresenter();
        super.onViewCreated(view, savedInstanceState);
    }

    protected abstract void initPresenter();
}
