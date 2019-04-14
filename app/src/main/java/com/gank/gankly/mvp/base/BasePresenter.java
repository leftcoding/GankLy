package com.gank.gankly.mvp.base;

import android.content.Context;
import android.lectcoding.ui.base.BaseContract;
import android.lectcoding.ui.base.BaseView;

import com.gank.gankly.R;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Create by LingYan on 2016-05-12
 */
public abstract class BasePresenter<E extends BaseView> extends BaseContract.Presenter {
    protected E view;
    protected Context context;
    protected String requestTag = UUID.randomUUID().toString();
    private AtomicBoolean isDestroy = new AtomicBoolean(false);
    private String errorTip;

    public BasePresenter(Context context, E view) {
        this.view = view;
        this.context = context;
        errorTip = context.getString(R.string.loading_error);
    }

    public void destroy() {
        onDestroy();
        isDestroy.set(true);
        view = null;
        context = null;
    }

    protected void showProgress() {
        showProgress(true);
    }

    protected void showProgress(boolean useProgress) {
        if (useProgress && view != null) {
            view.showProgress();
        }
    }

    protected void hideProgress() {
        if (view != null) {
            view.hideProgress();
        }
    }

    protected boolean isViewLife() {
        return view != null;
    }

    protected abstract void onDestroy();

    protected boolean isDestroy() {
        return isDestroy.get();
    }
}
