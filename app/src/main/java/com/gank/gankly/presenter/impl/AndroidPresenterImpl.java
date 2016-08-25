package com.gank.gankly.presenter.impl;

import android.app.Activity;

import com.gank.gankly.bean.GankResult;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.model.BaseModel;
import com.gank.gankly.model.impl.AndroidModelImpl;
import com.gank.gankly.network.api.GankApi;
import com.gank.gankly.presenter.BaseAsynDataSource;
import com.gank.gankly.presenter.IBaseRefreshPresenter;
import com.gank.gankly.presenter.ViewControl;
import com.gank.gankly.utils.CrashUtils;
import com.gank.gankly.view.IMeiziView;
import com.socks.library.KLog;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Create by LingYan on 2016-07-20
 * Email:137387869@qq.com
 */
public class AndroidPresenterImpl extends BaseAsynDataSource<IMeiziView<List<ResultsBean>>> implements
        IBaseRefreshPresenter {
    private BaseModel mModel;
    private ViewControl mViewControl = new ViewControl();

    public AndroidPresenterImpl(Activity mActivity, IMeiziView<List<ResultsBean>> view) {
        super(mActivity, view);
        mModel = new AndroidModelImpl();
    }

    @Override
    public void fetchMore() {
        super.fetchMore();
        if (isMore()) {
            mIView.showRefresh();
            fetchData();
        }
    }

    @Override
    public void fetchNew() {
        super.fetchNew();
        initFirstPage();
        final int mPage = getNextPage();
        final int limit = getLimit();
        final Observable<GankResult> androidGoods = GankApi.getInstance()
                .getGankService().fetchAndroidGoods(limit, mPage);
        Observable<GankResult> images = GankApi.getInstance()
                .getGankService().fetchBenefitsGoods(limit, mPage);

        Observable.zip(androidGoods, images, new Func2<GankResult, GankResult, GankResult>() {
            @Override
            public GankResult call(GankResult androidGoods, GankResult images) {
                MeiziArrayList.getInstance().addGiftItems(images.getResults());
                return androidGoods;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSubscriber());
    }

    @Override
    public void fetchData() {
        super.fetchData();
        final int mPage = getNextPage();
        final int limit = getLimit();
        mModel.fetchData(mPage, limit, getSubscriber());
    }

    private Subscriber<GankResult> getSubscriber() {
        final int mPage = getNextPage();
        final int limit = getLimit();
        return new Subscriber<GankResult>() {
            @Override
            public void onCompleted() {
                mIView.hideRefresh();
                mIView.showContent();
                setFirst(false);
                int nextPage = mPage + 1;
                setNextPage(nextPage);
            }

            @Override
            public void onError(Throwable e) {
                KLog.e(e);
                CrashUtils.crashReport(e);
                mIView.hideRefresh();
                mViewControl.onError(mPage, isFirst(), isNetworkAvailable(), mIView);
            }

            @Override
            public void onNext(GankResult gankResult) {
                mViewControl.onNext(mPage, limit, gankResult.getResults(), mIView, new ViewControl.CallBackViewShow() {
                    @Override
                    public void hasMore(boolean more) {
                        setHasMore(more);
                    }
                });
            }
        };
    }
}
