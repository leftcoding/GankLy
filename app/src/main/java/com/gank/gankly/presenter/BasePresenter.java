package com.gank.gankly.presenter;

import android.app.Activity;
import android.content.Context;

import com.gank.gankly.utils.NetworkUtils;

/**
 * Create by LingYan on 2016-05-12
 */
public abstract class BasePresenter<E> {
    protected E mIView;
    protected Context mActivity;
    private int mPage;
    private boolean hasMore;
    private int mLimit = 20;
    protected boolean isFirst = true;

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

    public int getPage() {
        return mPage;
    }

    public void setPage(int page) {
        mPage = page;
    }

    public int getLimit() {
        return mLimit;
    }

    public void setLimit(int limit) {
        mLimit = limit;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }
}
