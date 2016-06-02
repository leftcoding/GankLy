package com.gank.gankly.ui.presenter;

import android.app.Activity;
import android.content.Context;

import com.gank.gankly.ui.view.IBaseView;

/**
 * Create by LingYan on 2016-05-12
 */
public class BasePresenter<E extends IBaseView> {
    protected E mIView;
    protected Context mActivity;

    public BasePresenter(Activity mActivity, E view) {
        this.mIView = view;
        this.mActivity = mActivity;
    }
}
