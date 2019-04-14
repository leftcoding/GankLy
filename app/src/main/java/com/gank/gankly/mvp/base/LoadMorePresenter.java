package com.gank.gankly.mvp.base;

import android.content.Context;
import android.lectcoding.ui.base.BaseView;
import androidx.annotation.NonNull;

/**
 * Create by LingYan on 2017-10-12
 */

public abstract class LoadMorePresenter<E extends BaseView> extends BasePresenter<E> {

    public LoadMorePresenter(@NonNull Context context, E view) {
        super(context, view);
    }
}
