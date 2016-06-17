package com.gank.gankly.ui.presenter;

import android.app.Activity;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.bean.GankResult;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.network.api.GankApi;
import com.gank.gankly.ui.view.IIosView;
import com.socks.library.KLog;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Create by LingYan on 2016-05-24
 */
public class IosPresenter extends BasePresenter<IIosView> {
    private int limit = 20;
    private int mPage;
    private int mGirlCurPage = 1;
    private boolean isGirlLoadMore;

    public IosPresenter(Activity mActivity, IIosView view) {
        super(mActivity, view);
    }

    public void fetchDate(final int page) {
        this.mPage = page;
        Observable<GankResult> iosGoods = GankApi.getInstance()
                .getGankService().fetchIosGoods(limit, page);
        Observable<GankResult> image = GankApi.getInstance()
                .getGankService().fetchBenefitsGoods(limit, page);

        Observable.zip(iosGoods, image, new Func2<GankResult, GankResult, GankResult>() {
            @Override
            public GankResult call(GankResult gankResult, GankResult gankResult2) {
                List<ResultsBean> list = gankResult2.getResults();
//                MeiziArrayList.getInstance().addBeanAndPage(list, mPage);
                return gankResult;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GankResult>() {
                    @Override
                    public void onCompleted() {
                        mIView.hideRefresh();
                        mIView.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mIView.hideRefresh();
                        mIView.onError(e, "");
                    }

                    @Override
                    public void onNext(GankResult gankResult) {
                        toNext(page, gankResult);
                    }
                });
    }


    public void fetchNextGirl() {
        KLog.d("isGirlLoadMore:" + isGirlLoadMore);
        if (isGirlLoadMore) {
            fetchGirlDate(mGirlCurPage);
        }
    }

    public void fetchGirl() {
        mGirlCurPage = 1;
        fetchGirlDate(mGirlCurPage);
    }

    private void fetchGirlDate(final int page) {
        mIView.showRefresh();
        GankApi.getInstance().fetchWelfare(limit, page, new Subscriber<GankResult>() {
            @Override
            public void onCompleted() {
                mIView.hideRefresh();
                mIView.onCompleted();
                mGirlCurPage = mGirlCurPage + 1;
            }

            @Override
            public void onError(Throwable e) {
                KLog.e("onError，" + e);
                mIView.hideRefresh();
                boolean isError;
                if (!isNetworkAvailable()) {
                    isError = false;
                } else {
                    isError = true;
                }
                toError(page, isError, e);
            }

            @Override
            public void onNext(GankResult gankResult) {
                toNext(page, gankResult);
                MeiziArrayList.getInstance().addBeanAndPage(gankResult.getResults(), page);
            }
        });
    }

    private void toError(int page, boolean isError, Throwable e) {
        int resId = R.string.tip_server_error;

        if (page > 1) {
            if (!isError) {
                resId = R.string.loading_network_failure;
            }
            mIView.onError(e, App.getAppString(resId));
        } else {
            if (isError) {
                mIView.showError();
            } else {
                mIView.showDisNetWork();
            }
        }
    }

    private void toNext(int page, GankResult gankResult) {
        if (!gankResult.isEmpty()) {
            List<ResultsBean> list = gankResult.getResults();
            if (page == 1) {
                mIView.clear();
                mIView.refillDate(list);
            } else {
                mIView.appendMoreDate(list);
            }
            if (gankResult.getSize() < limit) {
                isGirlLoadMore = false;
                mIView.hasNoMoreDate();
            } else {
                isGirlLoadMore = true;
            }
        } else {
            isGirlLoadMore = false;
            if (page <= 1) {
                mIView.showEmpty();
            } else {
                mIView.hasNoMoreDate();
            }
        }
    }
}
