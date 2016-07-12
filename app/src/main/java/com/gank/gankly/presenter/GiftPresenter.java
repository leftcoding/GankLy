package com.gank.gankly.presenter;

import android.app.Activity;

import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.bean.GiftResult;
import com.gank.gankly.model.GiftModel;
import com.gank.gankly.model.impl.GiftModeImpl;
import com.gank.gankly.utils.CrashUtils;
import com.gank.gankly.view.IGiftView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Create by LingYan on 2016-06-13
 */
public class GiftPresenter extends BasePresenter<IGiftView> {
    private int mPages;
    private int progress;
    private boolean isUnSubscribe;
    private GiftModel mGiftModel;
    private List<GiftBean> girls = new ArrayList<>();

    public GiftPresenter(Activity mActivity, IGiftView view) {
        super(mActivity, view);
        mGiftModel = new GiftModeImpl();
    }

    public void fetchNew(final int page) {
        mGiftModel.fetchGiftPage(page, new Subscriber<GiftResult>() {
            @Override
            public void onCompleted() {
                int _page = page + 1;
                mIView.setNextPage(_page);
                mIView.hideRefresh();
                mIView.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                mIView.hideRefresh();
                CrashUtils.crashReport(e);
            }

            @Override
            public void onNext(GiftResult giftResult) {
                boolean isEmpty = true;
                if (giftResult != null) {
                    if (giftResult.getSize() != 0) {
                        isEmpty = false;
                    }
                }
                if (isEmpty) {
                    mIView.showEmpty();
                } else {
                    if (page == 1) {
                        mIView.clear();
                    }
                    mIView.refillDate(giftResult.getList());
                    mPages = giftResult.getNum();
                }
            }
        });
    }

    public void fetchNext(final int page) {
        if (page <= mPages) {
            fetchNew(page);
        }
    }

    public void fetchImagePageList(final String url) {
        initProgress();
        Subscriber<GiftResult> subscriber = new Subscriber<GiftResult>() {
            @Override
            public void onCompleted() {
                mIView.hideRefresh();
            }

            @Override
            public void onError(Throwable e) {
                mIView.hideRefresh();
                CrashUtils.crashReport(e);
            }

            @Override
            public void onNext(GiftResult giftResult) {
                if (giftResult != null && giftResult.getSize() > 0) {
                    mIView.setMax(giftResult.getSize());
                    fetchImagesList(giftResult.getList());
                }
            }
        };
        mGiftModel.fetchImagesPageList(url, subscriber);
    }

    private void fetchImagesList(List<GiftBean> list) {
        isUnSubscribe = false;
        mGiftModel.setIsUnSubscribe(isUnSubscribe);
        girls.clear();
        Subscriber<List<GiftBean>> subscriber = new Subscriber<List<GiftBean>>() {
            @Override
            public void onCompleted() {
                mIView.disDialog();
                mIView.refillImagesCount(girls);
                if (!isUnSubscribe) {
                    mIView.gotoBrowseActivity();
                } else {
                    isUnSubscribe = false;
                }
            }

            @Override
            public void onError(Throwable e) {
                mIView.hideRefresh();
                CrashUtils.crashReport(e);
            }

            @Override
            public void onNext(List<GiftBean> giftResult) {
                if (giftResult != null && giftResult.size() > 0) {
                    girls.addAll(giftResult);
                }
            }
        };
        mGiftModel.fetchImagesList(list, subscriber, mIView);
    }

    public void unSubscribe() {
        if (mGiftModel.getSubscription() != null) {
            mGiftModel.getSubscription().unsubscribe();
            isUnSubscribe = true;
            initProgress();
        }
    }

    public void initProgress() {
        progress = 0;
        mIView.setMax(progress);
        mIView.setProgress(progress);
    }

}
