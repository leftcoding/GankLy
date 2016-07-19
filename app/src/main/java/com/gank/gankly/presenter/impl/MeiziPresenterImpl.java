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
    private ViewShow viewShow;

    public MeiziPresenterImpl(Activity mActivity, IMeiziView<List<ResultsBean>> view) {
        super(mActivity, view);
        mModel = new MeiziModelImpl();
        viewShow = new ViewShow();
    }

    @Override
    public void fetchNew() {
        initFirstPage();
        fetchData();
    }

    @Override
    public void fetchMore() {
        if (isHasMore()) {
            mIView.showRefresh();
            fetchData();
        }
    }

    @Override
    public void fetchData() {
        final int page = getPage();
        mModel.fetchData(page, getLimit(), new Subscriber<GankResult>() {
            @Override
            public void onCompleted() {
                mIView.hideRefresh();
                mIView.showContent();
                setFirst(false);
                int nextPage = page + 1;
                setPage(nextPage);
            }

            @Override
            public void onError(Throwable e) {
                KLog.e(e);
                CrashUtils.crashReport(e);
                mIView.hideRefresh();
                viewShow.callError(page, isFirst(), isNetworkAvailable(), mIView);
            }

            @Override
            public void onNext(GankResult gankResult) {
                viewShow.callShow(page, getLimit(), gankResult.getResults(), mIView, new ViewShow.CallBackViewShow() {
                    @Override
                    public void hasMore(boolean more) {
                        setHasMore(more);
                    }
                });
                MeiziArrayList.getInstance().addBeanAndPage(gankResult.getResults(), page);
            }
        });
    }
}
