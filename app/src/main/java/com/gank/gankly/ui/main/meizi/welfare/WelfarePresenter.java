package com.gank.gankly.ui.main.meizi.welfare;

import com.gank.gankly.bean.GankResult;
import com.gank.gankly.mvp.FetchPresenter;
import com.gank.gankly.mvp.source.remote.GankDataSource;
import com.socks.library.KLog;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2016-12-23
 * Email:137387869@qq.com
 */

public class WelfarePresenter extends FetchPresenter implements WelfareContract.Presenter {
    private WelfareContract.View mModelView;
    private GankDataSource mTask;

    public WelfarePresenter(GankDataSource task, WelfareContract.View view) {
        mTask = task;
        mModelView = view;
    }

    @Override
    public void fetchNew() {
        fetchData(getInitPage());
    }

    private void fetchData(final int page) {
        mTask.fetchBenefitsGoods(page, getFetchLimit())
                .subscribe(new Observer<GankResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GankResult gankResult) {
                        if (page == 1) {
                            mModelView.refillData(gankResult.getResults());
                        } else {
                            mModelView.appendData(gankResult.getResults());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                    }

                    @Override
                    public void onComplete() {
                        mModelView.showContent();
                        setFetchPage(page + 1);
                    }
                });
    }

    @Override
    public void fetchMore() {
        fetchData(getFetchPage());
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }
}
