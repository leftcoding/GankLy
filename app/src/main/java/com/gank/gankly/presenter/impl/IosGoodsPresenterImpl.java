package com.gank.gankly.presenter.impl;

import android.app.Activity;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.model.IosModel;
import com.gank.gankly.model.impl.IosModelImpl;
import com.gank.gankly.presenter.BaseFetchDataPresenter;
import com.gank.gankly.presenter.RefreshPresenter;
import com.gank.gankly.view.IIosView;
import com.socks.library.KLog;

import java.util.List;

/**
 * Create by LingYan on 2016-06-20
 */
public class IosGoodsPresenterImpl extends BaseFetchDataPresenter<IIosView<ResultsBean>, ResultsBean>
        implements RefreshPresenter {
    private int limit = 20;
    private int mIosCurPage;
    private boolean isIosLoadMore;
    private IosModel mIosModel;

    public IosGoodsPresenterImpl(Activity mActivity, IIosView<ResultsBean> view) {
        super(mActivity, view);
        mIosModel = new IosModelImpl(this);
    }

    @Override
    public void fetchNew() {
        mIosCurPage = 1;
        fetchIosData();
    }

    @Override
    public void fetchMore() {
        if (isIosLoadMore) {
            fetchIosData();
        }
    }

    private void fetchIosData() {
        mIView.showRefresh();
        mIosModel.fetchIos(mIosCurPage, limit);
    }

    @Override
    public void onCompleted() {
        mIView.hideRefresh();
        mIView.onCompleted();
        mIosCurPage = mIosCurPage + 1;
    }

    @Override
    public void onError(Throwable e) {
        mIView.hideRefresh();
        int size = MeiziArrayList.getInstance().size();
        boolean isNetWork = isNetworkAvailable();
        if (mIosCurPage > 1 || size > 0) {
            int resId = R.string.loading_network_failure;
            if (isNetworkAvailable()) {
                resId = R.string.tip_server_error;
            }
            mIView.onError(e, App.getAppString(resId));
        } else {
            if (isNetWork) {
                mIView.showError();
            } else {
                mIView.showDisNetWork();
            }
        }
    }

    @Override
    public void onNext(List<ResultsBean> list) {
        KLog.d("list.size:" + list.size());
        if (mIosCurPage == 1) {
            mIView.clear();
            mIView.refillDate(list);
        } else {
            mIView.appendMoreDate(list);
        }
        if (list.size() < limit) {
            isIosLoadMore = false;
            mIView.hasNoMoreDate();
        } else {
            isIosLoadMore = true;
        }
    }

    @Override
    public void onEmpty() {
        isIosLoadMore = false;
        if (mIosCurPage <= 1) {
            mIView.showEmpty();
        } else {
            mIView.hasNoMoreDate();
        }
    }
}
