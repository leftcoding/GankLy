package com.gank.gankly.ui.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Create by LingYan on 2016-5-12
 */
public abstract class LazyFragment extends SupportFragment {
    private boolean isVisible = false; //是否可见
    private boolean isView = false; //是否建立视图关系
    private boolean isFirstLoad = true; //是否第一次加载

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isView = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
            lazyLoadData();
        } else {
            isVisible = false;
        }
    }

    protected abstract void initLazy();

    private void lazyLoadData() {
        if (!isVisible || !isFirstLoad || !isView) {
            return;
        }
        initLazy();
        isFirstLoad = false;
    }
}
