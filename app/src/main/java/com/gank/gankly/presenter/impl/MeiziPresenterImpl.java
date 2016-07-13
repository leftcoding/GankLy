package com.gank.gankly.presenter.impl;

import android.app.Activity;

import com.gank.gankly.bean.GankResult;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.model.BaseModel;
import com.gank.gankly.model.impl.MeiziModelImpl;
import com.gank.gankly.presenter.BasePresenter;
import com.gank.gankly.presenter.IBaseRefreshPresenter;
import com.gank.gankly.presenter.ViewShow;
import com.gank.gankly.utils.CrashUtils;
import com.gank.gankly.view.IMeiziView;

import java.util.List;

import rx.Subscriber;

/**
 * Create by LingYan on 2016-07-13
 * Email:137387869@qq.com
 */
public class MeiziPresenterImpl extends BasePresenter<IMeiziView<List<ResultsBean>>>
        implements IBaseRefreshPresenter {
    private int mPage = 1;
    private int limit = 20;
    private BaseModel mModel;
    private boolean hasMore = false;
    private ViewShow viewShow = new ViewShow();

    public MeiziPresenterImpl(Activity mActivity, IMeiziView<List<ResultsBean>> view) {
        super(mActivity, view);
        mModel = new MeiziModelImpl();
    }


    @Override
    public void fetchNew(int page) {
        mPage = page;
        mModel.fetchDate(page, limit, new Subscriber<GankResult>() {
            @Override
            public void onCompleted() {
                mIView.hideRefresh();
                mIView.showContent();
                mPage = mPage + 1;
                mIView.getNextPage(mPage);
            }

            @Override
            public void onError(Throwable e) {
                CrashUtils.crashReport(e);
                mIView.hideRefresh();
                viewShow.callError(mPage, isNetworkAvailable(), mIView);
            }

            @Override
            public void onNext(GankResult gankResult) {
                viewShow.callShow(mPage, limit, gankResult.getResults(), mIView, new ViewShow.CallBackViewShow() {
                    @Override
                    public void hasMore(boolean more) {
                        hasMore = more;
                    }
                });
                MeiziArrayList.getInstance().addBeanAndPage(gankResult.getResults(), mPage);
            }
        });
    }

    @Override
    public void fetchMore(int page) {
        if (hasMore) {
            mIView.showRefresh();
            fetchNew(page);
        }
    }
}
