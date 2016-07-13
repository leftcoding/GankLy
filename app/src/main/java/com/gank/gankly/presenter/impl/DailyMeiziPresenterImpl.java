package com.gank.gankly.presenter.impl;

import android.app.Activity;

import com.gank.gankly.bean.DailyMeiziBean;
import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.model.DailyMeiziModel;
import com.gank.gankly.model.impl.DailyMeiziModelImpl;
import com.gank.gankly.presenter.BasePresenter;
import com.gank.gankly.presenter.DailyMeiziPresenter;
import com.gank.gankly.utils.CrashUtils;
import com.gank.gankly.utils.ListUtils;
import com.gank.gankly.view.IDailyMeiziView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Create by LingYan on 2016-07-05
 */
public class DailyMeiziPresenterImpl extends BasePresenter<IDailyMeiziView<List<DailyMeiziBean>>>
        implements DailyMeiziPresenter {
    private DailyMeiziModel mModel;
    private List<GiftBean> mDailyMeiziBeanList = new ArrayList<>();

    public DailyMeiziPresenterImpl(Activity mActivity, IDailyMeiziView<List<DailyMeiziBean>> view) {
        super(mActivity, view);
        mModel = new DailyMeiziModelImpl();
    }


    @Override
    public void fetchNew() {
        mModel.fetchDailyMeizi(new Subscriber<List<DailyMeiziBean>>() {
            @Override
            public void onCompleted() {
                mIView.hideRefresh();
                mIView.showContent();
            }

            @Override
            public void onError(Throwable e) {
                CrashUtils.crashReport(e);
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
        int progress = 0;
        mIView.setMax(progress);
        mIView.setProgressValue(progress);
        mModel.setUnSubscribe(false);
        mModel.fetchImageUrls(url, new Subscriber<List<GiftBean>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                CrashUtils.crashReport(e);
            }

            @Override
            public void onNext(List<GiftBean> dailyMeiziBeen) {
                mIView.setMax(dailyMeiziBeen.size());
                fetchImageList(dailyMeiziBeen);
            }
        });
    }

    @Override
    public List<GiftBean> getList() {
        return mDailyMeiziBeanList;
    }

    @Override
    public void unSubscribe() {
        Subscription subscriptions = mModel.getSubscription();
        if (subscriptions != null && !subscriptions.isUnsubscribed()) {
            subscriptions.unsubscribe();
        }
    }

    private void fetchImageList(List<GiftBean> dailyMeiziBeen) {
        mDailyMeiziBeanList.clear();
        Action1<Integer> action1 = new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                mIView.setProgressValue(integer);
            }
        };
        mModel.fetchImageList(dailyMeiziBeen, action1, new Subscriber<List<GiftBean>>() {
            @Override
            public void onCompleted() {
                mIView.disDialog();
                if (!mModel.getUnSubscribe()) {
                    mIView.gotoBrowseActivity();
                }
            }

            @Override
            public void onError(Throwable e) {
                CrashUtils.crashReport(e);
            }

            @Override
            public void onNext(List<GiftBean> list) {
                if (!ListUtils.isListEmpty(list)) {
                    mDailyMeiziBeanList.addAll(list);
                }
            }
        });
    }
}
