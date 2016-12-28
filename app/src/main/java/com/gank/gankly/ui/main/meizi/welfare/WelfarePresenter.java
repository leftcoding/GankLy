package com.gank.gankly.ui.main.meizi.welfare;

import com.gank.gankly.bean.GankResult;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.mvp.FetchPresenter;
import com.gank.gankly.mvp.source.remote.GankDataSource;
import com.socks.library.KLog;

import java.util.List;

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
                        List<ResultsBean> list = filterData(gankResult.getResults(), mModelView);
                        if (list != null) {
                            if (page == 1) {
                                mModelView.refillData(list);
                            } else {
                                mModelView.appendData(list);
                            }
                            MeiziArrayList.getInstance().addBeanAndPage(list, page);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                        parseError(mModelView);
                    }

                    @Override
                    public void onComplete() {
                        setFetchPage(page + 1);
                    }
                });
    }

    @Override
    public void fetchMore() {
        if (hasMore()) {
            mModelView.showRefresh();
            fetchData(getFetchPage());
        }
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }
}
