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

    private PageResult<ResultsBean> pageResult;
    private PageConfig pageConfig;

    AndroidPresenter(Context context, AndroidContract.View view) {
        super(context, view);
        pageConfig = new PageConfig();
    }

    @Override
    protected void refreshAndroid() {
        fetchAndroid(INIT_PAGE);
    }

    @Override
    protected void appendAndroid() {
        if (pageResult != null && !pageResult.hasNoMore(pageConfig.mLimit)) {
            fetchAndroid(pageResult.nextPage);
        }
    }

    @Override
    public void unSubscribe() {
        super.unSubscribe();
        RxManager.get().clear(TAG);
        pageResult = null;
    }

    private void fetchAndroid(final int curPage) {
        pageConfig.mCurPage = curPage;

        GankServerManager.with(mContext)
                .androids(curPage, pageConfig.mLimit)
                .doOnSubscribe(disposable -> {
                    if (isActivityLife()) {
                        mView.showProgress();
                    }
                })
                .doFinally(() -> {
                    if (isActivityLife()) {
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
            if (isActivityLife()) {
                mView.showEmpty();
            }
        }

        @Override
        protected void appendEmpty() {
            if (isActivityLife()) {
                mView.hasNoMoreDate();
            }
        }

        @Override
        public void onComplete() {

        }

        @Override
        protected void refreshError() {
            super.refreshError();
            if (isActivityLife()) {
                mView.showError();
            }
        }

        @Override
        protected void appendError() {
            super.appendError();
            if (isActivityLife()) {
                mView.showShortToast(mContext.getString(R.string.loading_error));
            }
        }
    }

    private void parseAndroidData(final PageResult<ResultsBean> result) {
        if (!isActivityLife()) {
            return;
        }

        pageResult = result;
        pageResult.nextPage = getNextPage();
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
        return pageConfig.isFirstRequest();
    }

    private int getCurPage() {
        return pageConfig.mCurPage;
    }
}
