package com.gank.gankly.ui.base;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

import com.gank.gankly.butterknife.ButterKnifeFragment;


/**
 * Create by LingYan on 2018-08-24
 */

public abstract class LazyFragment extends ButterKnifeFragment {
    // 视图是否已创建，第一次，系统默认调用 setUserVisibleHint
    private boolean isVisibleView;

    // 视图是否已经加载过
    private boolean isPrepared;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isVisibleView = false;
        isPrepared = false;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isVisibleView = true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!isPrepared && getUserVisibleHint()) {
            onLazyCreate();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleView) {
            return;
        }
        if (!isPrepared && isVisibleToUser) {
            onLazyCreate();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isPrepared = false;
        isVisibleView = false;
    }

    private void onLazyCreate() {
        isPrepared = true;
        onLazyActivityCreate();
    }

    /**
     * 实现懒加载，代替原来的onActivityCreated方法，把原来逻辑，转移到这里
     */
    public abstract void onLazyActivityCreate();
}
