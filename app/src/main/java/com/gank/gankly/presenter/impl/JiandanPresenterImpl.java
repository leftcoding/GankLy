package com.gank.gankly.presenter.impl;

import android.app.Activity;

import com.gank.gankly.bean.JiandanResult;
import com.gank.gankly.model.JiandanModel;
import com.gank.gankly.model.impl.JiandanModelImpl;
import com.gank.gankly.presenter.BaseAsynDataSource;
import com.gank.gankly.presenter.ViewShow;
import com.gank.gankly.utils.CrashUtils;
import com.gank.gankly.view.IMeiziView;
import com.socks.library.KLog;

import java.util.List;

import rx.Subscriber;

/**
 * Create by LingYan on 2016-07-20
 * Email:137387869@qq.com
 */
public class JiandanPresenterImpl extends BaseAsynDataSource<IMeiziView<List<JiandanResult.PostsBean>>> {
    private JiandanModel mModel;
    private ViewShow mViewShow = new ViewShow();

    public JiandanPresenterImpl(Activity mActivity, IMeiziView<List<JiandanResult.PostsBean>> view) {
        super(mActivity, view);
        mModel = new JiandanModelImpl();
    }

    @Override
    public void fetchNew() {
        super.fetchNew();
        initFirstPage();
        fetchData();
    }

    @Override
    public void fetchData() {
        super.fetchData();
        final int mPage = getPage();
        mModel.fetchData(mPage, new Subscriber<JiandanResult>() {
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
                mIView.hideRefresh();
                CrashUtils.crashReport(e);
                mViewShow.callError(mPage, isFirst(), isNetworkAvailable(), mIView);
            }

            @Override
            public void onNext(JiandanResult result) {
                mViewShow.callShow(mPage, getLimit(), result.getPosts(), mIView, new ViewShow.CallBackViewShow() {
                    @Override
                    public void hasMore(boolean more) {
                        setHasMore(more);
                    }
                });
            }
        });
    }

    @Override
    public void fetchMore() {
        super.fetchMore();
        mIView.showRefresh();
        fetchData();
    }
}
