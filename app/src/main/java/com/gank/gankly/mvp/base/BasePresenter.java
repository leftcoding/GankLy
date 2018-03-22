package com.gank.gankly.mvp.base;

import android.content.Context;

import com.gank.gankly.R;
import com.gank.gankly.mvp.ISubscribePresenter;

import java.util.UUID;

/**
 * Create by LingYan on 2016-05-12
 */
public abstract class BasePresenter<E extends BaseView> extends BaseContract.Presenter implements ISubscribePresenter {
    protected E view;
    protected Context context;
    protected String requestTag = UUID.randomUUID().toString();
    private String errorTip;

    public BasePresenter(Context context, E view) {
        checkNotNull(context);
        checkNotNull(view);
        this.view = view;
        this.context = context;
        errorTip = context.getString(R.string.loading_error);
    }

    @SuppressWarnings("used")
    private <T> void checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
    }

    @Override
    public void unSubscribe() {
        view = null;
        context = null;
    }

    protected boolean isViewLife() {
        return view != null;
    }
}
