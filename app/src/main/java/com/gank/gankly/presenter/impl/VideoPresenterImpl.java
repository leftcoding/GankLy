package com.gank.gankly.presenter.impl;

import android.app.Activity;

import com.gank.gankly.bean.GankResult;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.model.BaseModel;
import com.gank.gankly.model.impl.VideoModelImpl;
import com.gank.gankly.presenter.BasePresenter;
import com.gank.gankly.presenter.IBaseRefreshPresenter;
import com.gank.gankly.presenter.ViewShow;
import com.gank.gankly.utils.CrashUtils;
import com.gank.gankly.utils.NetworkUtils;
import com.gank.gankly.view.IMeiziView;
import com.socks.library.KLog;

import java.util.List;

import rx.Subscriber;

/**
 * Create by LingYan on 2016-07-14
 * Email:137387869@qq.com
 */
public class VideoPresenterImpl extends BasePresenter<IMeiziView<List<ResultsBean>>> implements
        IBaseRefreshPresenter {
    private BaseModel mModel;
    private ViewShow mViewShow;

    public VideoPresenterImpl(Activity mActivity, IMeiziView<List<ResultsBean>> view) {
        super(mActivity, view);
        mModel = new VideoModelImpl();
        mViewShow = new ViewShow();
    }

    @Override
    public void fetchNew(final int page) {
        mIView.showRefresh();
        mModel.fetchDate(page, getLimit(), new Subscriber<GankResult>() {
            @Override
            public void onCompleted() {
                setFirst(false);
                setPage(page);
                mIView.hideRefresh();
                mIView.showContent();
                int mPage = page + 1;
                mIView.getNextPage(mPage);
            }

            @Override
            public void onError(Throwable e) {
                KLog.e(e);
                CrashUtils.crashReport(e);
                mIView.hideRefresh();
                mViewShow.callError(page, isFirst(), NetworkUtils.isNetworkAvailable(mActivity), mIView);
            }

            @Override
            public void onNext(GankResult gankResult) {
                mViewShow.callShow(page, getLimit(), gankResult.getResults(), mIView, new ViewShow.CallBackViewShow() {
                    @Override
                    public void hasMore(boolean more) {
                        setHasMore(more);
                    }
                });
            }
        });
    }

    @Override
    public void fetchMore(int page) {
        if (isHasMore()) {
            fetchNew(page);
        }
    }
}
