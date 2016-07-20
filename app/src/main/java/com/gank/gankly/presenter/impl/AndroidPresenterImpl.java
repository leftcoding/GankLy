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
import com.gank.gankly.presenter.ViewShow;
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
    private ViewShow mViewShow = new ViewShow();

    public AndroidPresenterImpl(Activity mActivity, IMeiziView<List<ResultsBean>> view) {
        super(mActivity, view);
        mModel = new AndroidModelImpl();
    }

    @Override
    public void fetchMore() {
        super.fetchMore();
        if (isHasMore()) {
            mIView.showRefresh();
            fetchData();
        }
    }

    @Override
    public void fetchNew() {
        super.fetchNew();
        initFirstPage();
        final int mPage = getPage();
        final Observable<GankResult> androidGoods = GankApi.getInstance()
                .getGankService().fetchAndroidGoods(getLimit(), mPage);
        Observable<GankResult> images = GankApi.getInstance()
                .getGankService().fetchBenefitsGoods(getLimit(), mPage);

        Observable.zip(androidGoods, images, new Func2<GankResult, GankResult, GankResult>() {
            @Override
            public GankResult call(GankResult androidGoods, GankResult images) {
                MeiziArrayList.getInstance().addGiftItems(images.getResults());
                return androidGoods;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GankResult>() {
                    @Override
                    public void onCompleted() {
                        mIView.hideRefresh();
                        mIView.showContent();
                        setFirst(false);
                        setPage(mPage + 1);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mIView.hideRefresh();
                        KLog.e(e);
                        CrashUtils.crashReport(e);
                        mViewShow.callError(mPage, isFirst(), isNetworkAvailable(), mIView);
                    }

                    @Override
                    public void onNext(GankResult gankResult) {
                        mViewShow.callShow(mPage, getLimit(), gankResult.getResults(), mIView, new ViewShow.CallBackViewShow() {
                            @Override
                            public void hasMore(boolean more) {
                                setHasMore(more);
                            }
                        });
                    }
                });
    }

    @Override
    public void fetchData() {
        super.fetchData();
        final int mPage = getPage();
        mModel.fetchData(mPage, getLimit(), new Subscriber<GankResult>() {
            @Override
            public void onCompleted() {
                mIView.hideRefresh();
                mIView.showContent();
                setFirst(false);
                setPage(mPage + 1);
            }

            @Override
            public void onError(Throwable e) {
                KLog.e(e);
                CrashUtils.crashReport(e);
                mIView.hideRefresh();
                mViewShow.callError(mPage, isFirst(), isNetworkAvailable(), mIView);
            }

            @Override
            public void onNext(GankResult gankResult) {
                mViewShow.callShow(mPage, getLimit(), gankResult.getResults(), mIView, new ViewShow.CallBackViewShow() {
                    @Override
                    public void hasMore(boolean more) {
                        setHasMore(more);
                    }
                });
            }
        });
    }
}
