package com.gank.gankly.ui.main.ios;

import android.support.annotation.NonNull;

import com.gank.gankly.bean.GankResult;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.mvp.FetchPresenter;
import com.gank.gankly.mvp.source.remote.GankDataSource;
import com.socks.library.KLog;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2016-12-20
 * Email:137387869@qq.com
 */

public class IosPresenter extends FetchPresenter implements IosContract.Presenter {
    @NonNull
    private GankDataSource mTask;
    @NonNull
    private IosContract.View mModelView;

    public IosPresenter(@NonNull GankDataSource gankDataSource, @NonNull IosContract.View view) {
        mTask = gankDataSource;
        mModelView = view;
    }

    @Override
    public void fetchNew() {
        fetchData(getInitPage());
    }

    @Override
    public void fetchMore() {
        fetchData(getFetchPage());
    }

    private void fetchData(final int page) {
        mTask.fetchIos(page, getFetchLimit())
                .subscribe(new Observer<GankResult>() {
                    @Override
                    public void onComplete() {
                        setFetchPage(page + 1);
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
                        List<ResultsBean> list = filterData(gankResult.getResults(), mModelView);
                        if (list != null) {
                            if (page == 1) {
                                mModelView.refillDate(list);
                            } else {
                                mModelView.appendData(list);
                            }
                        }
                    }
                });
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }
}
