package com.gank.gankly.presenter;

import android.app.Activity;

import com.gank.gankly.bean.GankResult;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.config.ViewStatus;
import com.gank.gankly.network.api.GankApi;
import com.gank.gankly.utils.CrashUtils;
import com.gank.gankly.view.IVideoView;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Create by LingYan on 2016-05-23
 */
public class VideoPresenter extends BasePresenter<IVideoView<List<ResultsBean>>> {
    private ViewStatus mViewStatus;
    private int mPage = 1;

    public VideoPresenter(Activity mActivity, IVideoView<List<ResultsBean>> view) {
        super(mActivity, view);
    }

    public void fetchDate(final int mPage, final int limit) {
        this.mPage = mPage;
        Observable<GankResult> video = GankApi.getInstance()
                .getGankService().fetchVideo(limit, mPage);
        Observable<GankResult> image = GankApi.getInstance()
                .getGankService().fetchBenefitsGoods(limit, mPage);
        Observable.zip(video, image, new Func2<GankResult, GankResult, GankResult>() {
            @Override
            public GankResult call(GankResult gankResult, GankResult gankResult2) {
                addImages(gankResult2, mPage);
                return gankResult;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSubscriber(mPage, limit));
    }

    private void addImages(GankResult gankResult, int mPage) {
        if (gankResult != null) {
            int page = MeiziArrayList.getInstance().getPage();
            if (page == 0 || page < mPage) {
                MeiziArrayList.getInstance().addBeanAndPage(gankResult.getResults(), mPage);
            }
        }
    }

    private Subscriber<GankResult> onSubscriber(final int page, final int limit) {
        return new Subscriber<GankResult>() {
            @Override
            public void onCompleted() {
                mIView.hideRefresh();
                if (mViewStatus != ViewStatus.SHOW) {
                    mIView.showContent();
                }
                mPage = page + 1;
                mIView.getNextPage(mPage);
            }

            @Override
            public void onError(Throwable e) {
                CrashUtils.crashReport(e);
                mIView.hideRefresh();
                mIView.showRefreshError("");
            }

            @Override
            public void onNext(GankResult gankResult) {
                if (!gankResult.isEmpty()) {
                    if (page == 1) {
                        mIView.refillDate(gankResult.getResults());
                    } else {
                        mIView.appendMoreDate(gankResult.getResults());
                    }
                }

                if (gankResult.getSize() < limit) {
                    mIView.hasNoMoreDate();
                }
            }
        };
    }

    public void setViewStatus(ViewStatus viewStatus) {
        mViewStatus = viewStatus;
    }
}
