package com.gank.gankly.mvp.base;

import android.content.Context;

import com.gank.gankly.mvp.ISubscribePresenter;

/**
 * Create by LingYan on 2016-05-12
 */
public abstract class BasePresenter<E extends BaseView> extends BaseContract.Presenter implements ISubscribePresenter {
    protected E mView;
    protected Context mContext;

    public BasePresenter(Context context, E view) {
        checkNotNull(context);
        checkNotNull(view);
        this.mView = view;
        this.mContext = context;
    }

    @SuppressWarnings("used")
    private static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    protected void showShortToast(String string) {
        if (mView != null) {
            mView.showShortToast(string);
        }
    }

    @Override
    public void unSubscribe() {
        mView = null;
        mContext = null;
    }

    protected boolean isActivityLife() {
        return mView != null;
    }
}
