package com.gank.gankly.mvp.base;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Create by LingYan on 2017-10-12
 */

public abstract class LoadMorePresenter<E extends BaseView> extends BasePresenter<E> {
    private int mInitPage = 1;
    private int mLimit = 20;

    public LoadMorePresenter(@NonNull Context context, E view) {
        super(context, view);
    }

    public int getInitPage() {
        return mInitPage;
    }

    public void setInitPage(int initPage) {
        mInitPage = initPage;
    }

    public int getLimit() {
        return mLimit;
    }

    public void setLimit(int limit) {
        mLimit = limit;
    }
}
