package com.gank.gankly.presenter.impl;

import android.app.Activity;

import com.gank.gankly.bean.GankResult;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.model.IosModel;
import com.gank.gankly.model.impl.IosModelImpl;
import com.gank.gankly.presenter.BaseAsynDataSource;
import com.gank.gankly.presenter.IBaseRefreshPresenter;
import com.gank.gankly.presenter.ViewControl;
import com.gank.gankly.utils.CrashUtils;
import com.gank.gankly.view.IMeiziView;
import com.socks.library.KLog;

import java.util.List;

import rx.Subscriber;

/**
 * Create by LingYan on 2016-06-20
 * Email:137387869@qq.com
 */
public class IosGoodsPresenterImpl extends BaseAsynDataSource<IMeiziView<List<ResultsBean>>> implements
        IBaseRefreshPresenter {
    private IosModel mIosModel;
    private ViewControl mViewControl;

    public IosGoodsPresenterImpl(Activity mActivity, IMeiziView<List<ResultsBean>> view) {
        super(mActivity, view);
        mIosModel = new IosModelImpl();
        mViewControl = new ViewControl();
    }

    @Override
    public void fetchData() {
        super.fetchData();
        final int page = getNextPage();
        final int limit = getLimit();
        mIosModel.fetchIos(page, limit, new Subscriber<GankResult>() {
            @Override
            public void onCompleted() {
                mIView.hideRefresh();
                mIView.showContent();
                int nextPage = page + 1;
                setNextPage(nextPage);
                setFirst(false);
            }

            @Override
            public void onError(Throwable e) {
                KLog.e(e);
                CrashUtils.crashReport(e);
                mIView.hideRefresh();
                mViewControl.onError(page, isFirst(), isNetworkAvailable(), mIView);
            }

            @Override
            public void onNext(GankResult gankResult) {
                mViewControl.onNext(page, limit, gankResult.getResults(), mIView, new ViewControl.CallBackViewShow() {
                    @Override
                    public void hasMore(boolean more) {
                        setHasMore(more);
                    }
                });
            }
        });
    }

    @Override
    public void fetchNew() {
        initFirstPage();
        fetchData();
    }

    @Override
    public void fetchMore() {
        if (isMore()) {
            mIView.showRefresh();
            fetchData();
        }
    }
}
