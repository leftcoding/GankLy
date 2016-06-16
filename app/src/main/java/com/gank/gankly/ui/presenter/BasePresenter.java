package com.gank.gankly.ui.presenter;

import android.app.Activity;
import android.content.Context;

import com.gank.gankly.ui.view.IBaseView;
import com.gank.gankly.utils.NetworkUtils;

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

    public Context getActivity() {
        return mActivity;
    }

    public boolean isNetworkAvailable() {
        return NetworkUtils.isNetworkAvailable(mActivity);
    }
}
