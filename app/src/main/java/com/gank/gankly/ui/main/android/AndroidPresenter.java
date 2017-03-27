package com.gank.gankly.ui.main.android;

import android.support.annotation.NonNull;

import com.gank.gankly.bean.GankResult;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.mvp.FetchPresenter;
import com.gank.gankly.mvp.source.remote.GankDataSource;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2016-10-25
 * Email:137387869@qq.com
 */

public class AndroidPresenter extends FetchPresenter implements AndroidContract.Presenter {
    @NonNull
    private final GankDataSource mTask;
    @NonNull
    private final AndroidContract.View mModelView;

    private final List<ResultsBean> mResultsBeanList;

    public AndroidPresenter(@NonNull GankDataSource gankDataSource, @NonNull AndroidContract.View view) {
        mTask = gankDataSource;
        mModelView = view;
        mResultsBeanList = new ArrayList<>();
    }

    @Override
    public void fetchNew() {
        fetchAndroid(getInitPage());
    }

    @Override
    public void fetchMore() {
        if (hasMore()) {
            mModelView.showRefresh();
            fetchAndroid(getFetchPage());
        }
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    private void fetchAndroid(final int page) {
        Observable<GankResult> observable;
        if (MeiziArrayList.getInstance().isOneItemsEmpty()) {
            observable = mTask.fetchAndroidAndImages(page, getFetchLimit());
        } else {
            observable = mTask.fetchAndroid(page, getFetchLimit());
        }

        observable.subscribe(new Observer<GankResult>() {
            @Override
            public void onComplete() {
                int nextPage = page + 1;
                setFetchPage(nextPage);
            }

            @Override
            public void onError(Throwable e) {
                KLog.e(e);
                parseError(mModelView);
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(GankResult gankResult) {
                parseAndroidData(gankResult);
            }
        });
    }

    private void parseAndroidData(GankResult gankResult) {
        List<ResultsBean> list = filterData(gankResult, mModelView);
        if (list != null) {
            if (getFetchPage() == 1) {
                mResultsBeanList.clear();
                mModelView.refillDate(list);
            } else {
                mModelView.appendData(list);
            }
            mResultsBeanList.addAll(list);
        }
    }

    @Override
    public List<ResultsBean> getResultList() {
        return null;
    }
}
