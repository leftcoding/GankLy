package com.gank.gankly.ui.presenter;

import android.app.Activity;

import com.gank.gankly.bean.GankResult;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.config.ViewStatus;
import com.gank.gankly.network.GankRetrofit;
import com.gank.gankly.ui.view.IVideoView;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Create by LingYan on 2016-05-23
 */
public class VideoPresenter extends BasePresenter<IVideoView> {
    public VideoPresenter(Activity mActivity, IVideoView view) {
        super(mActivity, view);
    }

    public void fetchDate(final int mPage, final int limit) {
        Observable<GankResult> video = GankRetrofit.getInstance()
                .getGankService().fetchVideo(limit, mPage);
        Observable<GankResult> image = GankRetrofit.getInstance()
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

    private Subscriber<GankResult> onSubscriber(final int mPage, final int limit) {
        return new Subscriber<GankResult>() {
            @Override
            public void onCompleted() {
                mIView.hideRefresh();
                if (mIView.getCurViewStatus() != ViewStatus.SHOW) {
                    mIView.showView();
                }
                mIView.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                mIView.hideRefresh();
                mIView.onError(e);
            }

            @Override
            public void onNext(GankResult gankResult) {
                if (!gankResult.isEmpty()) {
                    if (mPage == 1) {
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
}
