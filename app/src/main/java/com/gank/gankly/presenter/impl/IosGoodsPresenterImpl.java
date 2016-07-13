package com.gank.gankly.presenter.impl;

import android.app.Activity;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.bean.GankResult;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.model.IosModel;
import com.gank.gankly.model.impl.IosModelImpl;
import com.gank.gankly.presenter.BasePresenter;
import com.gank.gankly.presenter.IosGoodsPresenter;
import com.gank.gankly.utils.CrashUtils;
import com.gank.gankly.view.IIosView;

import java.util.List;

import rx.Subscriber;

/**
 * Create by LingYan on 2016-06-20
 */
public class IosGoodsPresenterImpl extends BasePresenter<IIosView<List<ResultsBean>>>
        implements IosGoodsPresenter {
    private int limit = 20;
    private int mIosCurPage;
    private boolean isIosLoadMore;
    private IosModel mIosModel;

    public IosGoodsPresenterImpl(Activity mActivity, IIosView<List<ResultsBean>> view) {
        super(mActivity, view);
        mIosModel = new IosModelImpl();
    }

    private void fetchIosData(int page) {
        mIView.showRefresh();
        mIosModel.fetchIos(page, limit, new Subscriber<GankResult>() {
            @Override
            public void onCompleted() {
                mIView.hideRefresh();
                mIView.showContent();
                mIosCurPage = mIosCurPage + 1;
                mIView.getNextPage(mIosCurPage);
            }

            @Override
            public void onError(Throwable e) {
                CrashUtils.crashReport(e);
                mIView.hideRefresh();
                int size = MeiziArrayList.getInstance().size();
                boolean isNetWork = isNetworkAvailable();
                if (mIosCurPage > 1 || size > 0) {
                    int resId = R.string.loading_network_failure;
                    if (isNetworkAvailable()) {
                        resId = R.string.tip_server_error;
                    }
                    mIView.showRefreshError(App.getAppString(resId));
                } else {
                    if (isNetWork) {
                        isIosLoadMore = false;
                        if (mIosCurPage <= 1) {
                            mIView.showEmpty();
                        } else {
                            mIView.hasNoMoreDate();
                        }
                    } else {
                        mIView.showDisNetWork();
                    }
                }
            }

            @Override
            public void onNext(GankResult gankResult) {
                boolean isEmpty = true;
                List<ResultsBean> list = null;
                if (gankResult != null) {
                    list = gankResult.getResults();
                    if (gankResult.getSize() != 0) {
                        isEmpty = false;
                    }
                }

                if (isEmpty) {
                    mIView.showEmpty();
                } else {
                    if (mIosCurPage == 1) {
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
            }
        });
    }

    @Override
    public void fetchNew(int page) {
        mIosCurPage = page;
        fetchIosData(page);
    }

    @Override
    public void fetchMore(int page) {
        if (isIosLoadMore) {
            fetchIosData(page);
        }
    }
}
