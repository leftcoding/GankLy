package com.gank.gankly.mvp;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gank.gankly.mvp.base.BasePresenter;
import com.gank.gankly.mvp.base.BaseView;

/**
 * Create by LingYan on 2017-10-05
 */

public class SubscribePresenter<E extends BaseView> extends BasePresenter<E> {

    public SubscribePresenter(@NonNull Context context, @NonNull E view) {
        super(context, view);
    }

    @Override
    protected void onDestroy() {

    }
}
