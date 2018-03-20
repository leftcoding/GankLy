package com.gank.gankly.ui.ios;

import android.content.Context;
import android.support.annotation.NonNull;

import com.leftcoding.network.api.GankServerManager;
import com.leftcoding.network.domain.PageConfig;
import com.leftcoding.network.domain.PageResult;
import com.leftcoding.network.domain.ResultsBean;
import com.leftcoding.rxbus.RxManager;
import com.socks.library.KLog;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import retrofit2.Response;

/**
 * Create by LingYan on 2016-12-20
 */

public class IosPresenter extends IosContract.Presenter {
    private static final String TAG = "ios_presenter";
    private static final int INIT_PAGE = 1;

    private PageResult<ResultsBean> mPageResult;
    private AtomicBoolean first = new AtomicBoolean(true);

    private PageConfig mPageConfig;

    public IosPresenter(@NonNull Context context, IosContract.View view) {
        super(context, view);
        mPageConfig = new PageConfig();
    }

    @Override
    void refreshIos() {
        fetchData(INIT_PAGE);
    }

    @Override
    void appendIos() {
        if (mPageResult != null) {
            fetchData(mPageResult.nextPage);
        }
    }

    private void fetchData(final int page) {
        mPageConfig.mCurPage = page;

        GankServerManager.with(mContext)
                .ios(page, mPageConfig.mLimit)
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
                .flatMap(new Function<Response<PageResult<ResultsBean>>, ObservableSource<PageResult<ResultsBean>>>() {
                    @Override
                    public ObservableSource<PageResult<ResultsBean>> apply(@io.reactivex.annotations.NonNull Response<PageResult<ResultsBean>> response) throws Exception {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    return Observable.create(e -> e.onNext(response.body()));
                                }
                            }
                        }
                        return Observable.error(new Throwable());
                    }
                })
                .map(new Function<PageResult<ResultsBean>, PageResult<ResultsBean>>() {
                    @Override
                    public PageResult<ResultsBean> apply(@io.reactivex.annotations.NonNull PageResult<ResultsBean> result) throws Exception {
                        if (result != null) {
                            return result;
                        }
                        return null;
                    }
                })
                .flatMap(new Function<PageResult<ResultsBean>, ObservableSource<PageResult<ResultsBean>>>() {
                    @Override
                    public ObservableSource<PageResult<ResultsBean>> apply(@io.reactivex.annotations.NonNull PageResult<ResultsBean> result) throws Exception {
                        if (result != null && result.results != null) {
                            return Observable.create(e -> e.onNext(result));
                        }
                        return Observable.error(new Throwable());
                    }
                })
                .subscribe(new Observer<PageResult<ResultsBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PageResult<ResultsBean> result) {
                        mPageResult = result;
                        mPageResult.nextPage = getNextPage();

                        if (isActivityLife()) {
                            if (isFirst()) {
                                mView.refreshIosSuccess(result.results);
                            } else {
                                mView.appendIosSuccess(result.results);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void unSubscribe() {
        super.unSubscribe();
        mPageResult = null;
        RxManager.get().clear(TAG);
    }

    private boolean isFirst() {
        return first.get();
    }

    private int getNextPage() {
        return mPageConfig.mCurPage + 1;
    }
}
