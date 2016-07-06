package com.gank.gankly.presenter.impl;

import android.app.Activity;

import com.gank.gankly.bean.DailyMeiziBean;
import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.model.DailyMeiziModel;
import com.gank.gankly.model.impl.DailyMeiziModelImpl;
import com.gank.gankly.presenter.BaseFetchDataPresenter;
import com.gank.gankly.presenter.DailyMeiziPresenter;
import com.gank.gankly.utils.ListUtils;
import com.gank.gankly.view.IDailyMeiziView;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Create by LingYan on 2016-07-05
 */
public class DailyMeiziPresenterImpl extends BaseFetchDataPresenter<IDailyMeiziView<DailyMeiziBean>, DailyMeiziBean>
        implements DailyMeiziPresenter {
    private DailyMeiziModel mModel;
    private List<GiftBean> mDailyMeiziBeanList = new ArrayList<>();

    public DailyMeiziPresenterImpl(Activity mActivity, IDailyMeiziView<DailyMeiziBean> view) {
        super(mActivity, view);
        mModel = new DailyMeiziModelImpl();
    }


    @Override
    public void fetchNew() {
        mModel.fetchDailyMeizi(new Subscriber<List<DailyMeiziBean>>() {
            @Override
            public void onCompleted() {
                mIView.hideRefresh();
            }

            @Override
            public void onError(Throwable e) {
                KLog.e(e);
            }

            @Override
            public void onNext(List<DailyMeiziBean> giftResult) {
                if (ListUtils.getListSize(giftResult) > 0) {
                    mIView.refillDate(giftResult);
                } else {
                    mIView.showEmpty();
                }
            }
        });
    }

    @Override
    public void fetchMore() {

    }

    @Override
    public void fetchImageUrls(String url) {
        mModel.fetchImageUrls(url, new Subscriber<List<GiftBean>>() {
            @Override
            public void onCompleted() {
                KLog.d("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                KLog.e(e);
            }

            @Override
            public void onNext(List<GiftBean> dailyMeiziBeen) {
                fetchImageList(dailyMeiziBeen);
            }
        });
    }

    @Override
    public List<GiftBean> getList() {
        return mDailyMeiziBeanList;
    }

    private void fetchImageList(List<GiftBean> dailyMeiziBeen) {
        mDailyMeiziBeanList.clear();
        mModel.fetchImageList(dailyMeiziBeen, new Subscriber<List<GiftBean>>() {
            @Override
            public void onCompleted() {
                mIView.disDialog();
                mIView.gotoBrowseActivity();
            }

            @Override
            public void onError(Throwable e) {
                KLog.e(e);
            }

            @Override
            public void onNext(List<GiftBean> list) {
                mDailyMeiziBeanList.addAll(list);
            }
        });
    }
}
