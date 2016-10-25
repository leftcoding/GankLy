package com.gank.gankly.ui.main.android;

import android.support.annotation.NonNull;

import com.gank.gankly.bean.GankResult;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.mvp.FetchPresenter;
import com.gank.gankly.mvp.source.remote.RemoteDataSource;
import com.socks.library.KLog;

import java.util.List;

import rx.Subscriber;

/**
 * Create by LingYan on 2016-10-25
 * Email:137387869@qq.com
 */

public class AndroidPresenter extends FetchPresenter implements AndroidContract.Presenter {
    @NonNull
    private RemoteDataSource mTask;
    @NonNull
    private AndroidContract.View mModelView;

    public AndroidPresenter(@NonNull RemoteDataSource remoteDataSource, @NonNull AndroidContract.View view) {
        mTask = remoteDataSource;
        mModelView = view;
    }

    @Override
    public void fetchNew() {
        taskAndroid(getInitPage());
    }

    @Override
    public void fetchMore() {
        taskAndroid(getFetchPage());
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    private void taskAndroid(final int page) {
        mTask.fetchAndroid(page, getFetchLimit())
                .subscribe(new Subscriber<GankResult>() {
                    @Override
                    public void onCompleted() {
                        int nextPage = page + 1;
                        setFetchPage(nextPage);
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                        parseError(mModelView);
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
                mModelView.refillDate(list);
            } else {
                mModelView.appendData(list);
            }
        }
    }
}
