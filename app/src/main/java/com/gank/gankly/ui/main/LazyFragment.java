package com.gank.gankly.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gank.gankly.ui.base.BaseFragment;
import com.socks.library.KLog;

public abstract class LazyFragment extends BaseFragment {
    private boolean isVisible = false; //是否可见
    private boolean isView = false; //是否建立视图关系
    private boolean isFirstLoad = true; //是否第一次加载

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isView = true;
        lazyLoadData();
    }

    @Override
    protected void initValues() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void bindLister() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            isVisible = true;
            lazyLoadData();
        } else {
            isVisible = false;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    protected abstract void initDate();

    private void lazyLoadData() {
        KLog.d("isVisible:" + isVisible + ",isFirstLoad:" + isFirstLoad + ",isView:" + isView);
        if (!isVisible || !isFirstLoad || !isView) {
            return;
        }
        KLog.d("完成数据第一次加载" + "   " + this.getClass().getSimpleName());
        initDate();
        isFirstLoad = false;
    }
}
