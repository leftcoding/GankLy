package com.gank.gankly.ui.android;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gank.gankly.mvp.observer.PageObserver;
import com.gank.gankly.ui.android.AndroidContract.Presenter;
import com.leftcoding.http.api.GankManager;
import com.leftcoding.http.bean.PageResult;
import com.leftcoding.http.bean.ResultsBean;
import com.leftcoding.rxbus.RxManager;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import retrofit2.Response;

/**
 * Create by LingYan on 2016-10-25
 */
class AndroidPresenter extends Presenter {
    private static final String TAG = "android_presenter";

    private PageResult<ResultsBean> mPageResult;

    private final AtomicBoolean first = new AtomicBoolean(true);

    AndroidPresenter(@NonNull Context context, @NonNull AndroidContract.View view) {
        super(context, view);
    }

    @Override
    protected void refreshAndroid() {
        fetchAndroid(getInitPage());
    }

    @Override
    protected void appendAndroid() {
        if (mPageResult != null) {
            fetchAndroid(mPageResult.nextPage);
        }
    }

    @Override
    public void unSubscribe() {
        super.unSubscribe();
        first.set(true);
        mPageResult = null;
        RxManager.get().clear(TAG);
    }

    private void fetchAndroid(final int page) {
        GankManager.with(mContext)
                .androids(page, getLimit())
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
                .flatMap(new Function<Response<PageResult<ResultsBean>>, ObservableSource<PageResult<ResultsBean>>>() {
                    @Override
                    public ObservableSource<PageResult<ResultsBean>> apply(Response<PageResult<ResultsBean>> pageResultResponse) throws Exception {
                        if (pageResultResponse == null
                                || !pageResultResponse.isSuccessful()
                                || pageResultResponse.body() == null) {
                            return Observable.error(new Throwable());
                        }
                        return Observable.create(e -> e.onNext(pageResultResponse.body()));
                    }
                })
                .map(pageResult -> {
                    if (pageResult != null) {
                        pageResult.curPage = page;
                        return pageResult;
                    }
                    return null;
                })
                .flatMap(new Function<PageResult<ResultsBean>, ObservableSource<PageResult<ResultsBean>>>() {
                    @Override
                    public ObservableSource<PageResult<ResultsBean>> apply(PageResult<ResultsBean> result) throws Exception {
                        if (result == null) {
                            return Observable.error(new Throwable());
                        }
                        return Observable.create(e -> e.onNext(result));
                    }
                })
                .subscribe(new PageSubscribe());
    }

    private class PageSubscribe extends PageObserver<PageResult<ResultsBean>> {
        PageSubscribe() {
            this(first.get());
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
        public void onError(@NonNull Throwable e) {
            super.onError(e);
            if (isActivity()) {
                if (isFirst()) {
                    mView.showEmpty();
                } else {
                    mView.appendAndroidFailure("网络请求失败.");
                }
            }
        }

        @Override
        public void onComplete() {
            first.set(false);
        }

        @Override
        public void refreshError(String str) {
            super.refreshError(str);
            if (isActivity()) {
                mView.showEmpty();
            }
        }

        @Override
        public void appendError(String str) {
            super.appendError(str);
            showShortToast(str);
        }
    }

    private void parseAndroidData(final PageResult<ResultsBean> result) {
        if (!isActivity()) {
            return;
        }

        mPageResult = result;
        mPageResult.nextPage = getNextPage(mPageResult.curPage);
        if (first.get()) {
            mView.refreshAndroidSuccess(result.results);
        } else {
            mView.appendAndroidSuccess(result.results);
        }
    }

    private int getNextPage(int curPage) {
        return curPage + 1;
    }

    private boolean isFirst() {
        return first.get();
    }
}
