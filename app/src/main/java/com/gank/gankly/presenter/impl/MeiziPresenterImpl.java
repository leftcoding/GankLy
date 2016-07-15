package com.gank.gankly.presenter.impl;

import android.app.Activity;

import com.gank.gankly.bean.GankResult;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.model.BaseModel;
import com.gank.gankly.model.impl.MeiziModelImpl;
import com.gank.gankly.presenter.BaseAsynDataSource;
import com.gank.gankly.presenter.IBaseRefreshPresenter;
import com.gank.gankly.presenter.ViewShow;
import com.gank.gankly.utils.CrashUtils;
import com.gank.gankly.view.IMeiziView;
import com.socks.library.KLog;

import java.util.List;

import rx.Subscriber;

/**
 * Create by LingYan on 2016-07-13
 * Email:137387869@qq.com
 */
public class MeiziPresenterImpl extends BaseAsynDataSource<IMeiziView<List<ResultsBean>>>
        implements IBaseRefreshPresenter {
    private BaseModel mModel;
    private ViewShow viewShow = new ViewShow();

    public MeiziPresenterImpl(Activity mActivity, IMeiziView<List<ResultsBean>> view) {
        super(mActivity, view);
        mModel = new MeiziModelImpl();
    }


    @Override
    public void fetchNew(final int mPage) {
        mModel.fetchData(mPage, getLimit(), new Subscriber<GankResult>() {
            @Override
            public void onCompleted() {
                setFirst(false);
                mIView.hideRefresh();
                mIView.showContent();
                int page = mPage + 1;
                mIView.setNextPage(page);
            }

            @Override
            public void onError(Throwable e) {
                KLog.e(e);
                CrashUtils.crashReport(e);
                mIView.hideRefresh();
                viewShow.callError(mPage, isFirst(), isNetworkAvailable(), mIView);
            }

            @Override
            public void onNext(GankResult gankResult) {
                viewShow.callShow(mPage, getLimit(), gankResult.getResults(), mIView, new ViewShow.CallBackViewShow() {
                    @Override
                    public void hasMore(boolean more) {
                        setHasMore(more);
                    }
                });
                MeiziArrayList.getInstance().addBeanAndPage(gankResult.getResults(), mPage);
            }
        });
    }

    @Override
    public void fetchMore(int page) {
        if (isHasMore()) {
            mIView.showRefresh();
            fetchNew(page);
        }
    }
}
