package com.gank.gankly.presenter.impl;

import android.app.Activity;

import com.gank.gankly.bean.JiandanResult;
import com.gank.gankly.model.JiandanModel;
import com.gank.gankly.model.impl.JiandanModelImpl;
import com.gank.gankly.presenter.BaseAsynDataSource;
import com.gank.gankly.presenter.ViewControl;
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
    private ViewControl mViewControl = new ViewControl();

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
        final int mPage = getNextPage();
        mModel.fetchData(mPage, new Subscriber<JiandanResult>() {
            @Override
            public void onCompleted() {
                mIView.hideRefresh();
                mIView.showContent();
                setFirst(false);
                setNextPage(mPage + 1);
            }

            @Override
            public void onError(Throwable e) {
                KLog.e(e);
                mIView.hideRefresh();
                CrashUtils.crashReport(e);
                mViewControl.onError(mPage, isFirst(), isNetworkAvailable(), mIView);
            }

            @Override
            public void onNext(JiandanResult result) {
                mViewControl.onNext(mPage, getLimit(), result.getPosts(), mIView, new ViewControl.CallBackViewShow() {
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
