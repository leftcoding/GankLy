package com.gank.gankly.ui.android;

import android.content.Context;

import com.gank.gankly.R;
import com.gank.gankly.mvp.observer.PageObserver;
import com.gank.gankly.ui.android.AndroidContract.Presenter;
import com.leftcoding.http.api.GankServerManager;
import com.leftcoding.http.bean.PageConfig;
import com.leftcoding.http.bean.PageResult;
import com.leftcoding.http.bean.ResultsBean;
import com.leftcoding.rxbus.RxManager;

/**
 * Create by LingYan on 2016-10-25
 */
class AndroidPresenter extends Presenter {
    private static final String TAG = "android_presenter";
    private static final int INIT_PAGE = 1;

    private PageResult<ResultsBean> mPageResult;
    private PageConfig mPageConfig;

    AndroidPresenter(Context context, AndroidContract.View view) {
        super(context, view);
        mPageConfig = new PageConfig();
    }

    @Override
    protected void refreshAndroid() {
        fetchAndroid(INIT_PAGE);
    }

    @Override
    protected void appendAndroid() {
        if (mPageResult != null && !mPageResult.hasNoMore(mPageConfig.mLimit)) {
            fetchAndroid(mPageResult.mNextPage);
        }
    }

    @Override
    public void unSubscribe() {
        super.unSubscribe();
        mPageResult = null;
        RxManager.get().clear(TAG);
    }

    private void fetchAndroid(final int curPage) {
        mPageConfig.mCurPage = curPage;

        GankServerManager.with(mContext)
                .androids(curPage, mPageConfig.mLimit)
                .doOnSubscribe(disposable -> {
                    if (isActivity()) {
                        mView.showProgress();
                    }
                })
                .doFinally(() -> {
                    if (isActivity()) {
                        mView.hideProgress();
                    }
                })
                .subscribe(new PageSubscribe());
    }

    private class PageSubscribe extends PageObserver<PageResult<ResultsBean>> {
        PageSubscribe() {
            this(isRefreshRequest());
        }

        PageSubscribe(boolean isFirst) {
            super(TAG, isFirst);
        }

        @Override
        public void onNext(PageResult<ResultsBean> results) {
            super.onNext(results);
            parseAndroidData(results);
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        protected void refreshEmpty() {
            if (isActivity()) {
                mView.showEmpty();
            }
        }

        @Override
        protected void appendEmpty() {
            if (isActivity()) {
                mView.hasNoMoreDate();
            }
        }

        @Override
        public void onComplete() {

        }

        @Override
        protected void refreshError() {
            super.refreshError();
            if (isActivity()) {
                mView.showError();
            }
        }

        @Override
        protected void appendError() {
            super.appendError();
            if (isActivity()) {
                mView.showShortToast(mContext.getString(R.string.loading_error));
            }
        }
    }

    private void parseAndroidData(final PageResult<ResultsBean> result) {
        if (!isActivity()) {
            return;
        }

        mPageResult = result;
        mPageResult.mNextPage = getNextPage();
        mView.showContent();
        if (isRefreshRequest()) {
            mView.refreshAndroidSuccess(result.results);
        } else {
            mView.appendAndroidSuccess(result.results);
        }
    }

    private int getNextPage() {
        return getCurPage() + 1;
    }

    private boolean isRefreshRequest() {
        return mPageConfig.isFirstRequest();
    }

    private int getCurPage() {
        return mPageConfig.mCurPage;
    }
}
