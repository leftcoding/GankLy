package com.gank.gankly.presenter.impl;

import android.app.Activity;

import com.gank.gankly.bean.GankResult;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.model.BaseModel;
import com.gank.gankly.model.impl.VideoModelImpl;
import com.gank.gankly.presenter.BaseAsynDataSource;
import com.gank.gankly.presenter.ViewControl;
import com.gank.gankly.utils.CrashUtils;
import com.gank.gankly.view.IMeiziView;
import com.socks.library.KLog;

import java.util.List;

import rx.Subscriber;

/**
 * Create by LingYan on 2016-07-14
 * Email:137387869@qq.com
 */
public class VideoPresenterImpl extends BaseAsynDataSource<IMeiziView<List<ResultsBean>>> {
    private BaseModel mModel;
    private ViewControl mViewControl;

    public VideoPresenterImpl(Activity mActivity, IMeiziView<List<ResultsBean>> view) {
        super(mActivity, view);
        mModel = new VideoModelImpl();
        mViewControl = new ViewControl();
    }

    @Override
    public void fetchNew() {
        initFirstPage();
        fetchData();
    }

    @Override
    public void fetchMore() {
        if (isMore()) {
            fetchData();
        }
    }

    @Override
    public void fetchData() {
        super.fetchData();
        mIView.showRefresh();
        final int page = getNextPage();
        mModel.fetchData(page, getLimit(), new Subscriber<GankResult>() {
            @Override
            public void onCompleted() {
                mIView.hideRefresh();
                mIView.showContent();
                setFirst(false);
                int mPage = page + 1;
                setNextPage(mPage);
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
                mViewControl.onNext(page, getLimit(), gankResult.getResults(), mIView, new ViewControl.CallBackViewShow() {
                    @Override
                    public void hasMore(boolean more) {
                        setHasMore(more);
                    }
                });
            }
        });
    }
}
