package com.gank.gankly.mvp.base;

import android.lectcoding.ui.base.BaseView;

/**
 * Create by LingYan on 2016-10-20
 */

public interface SupportView extends BaseView {
    void hasNoMoreDate();

    void showContent();

    void showEmpty();

    void showDisNetWork();

    void showError();
}
