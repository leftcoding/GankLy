package com.gank.gankly.presenter;

import android.app.Activity;

import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.bean.GiftResult;
import com.gank.gankly.model.GiftModel;
import com.gank.gankly.model.impl.GiftModeImpl;
import com.gank.gankly.utils.CrashUtils;
import com.gank.gankly.view.IGiftView;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2016-06-13
 */
public class GiftPresenter extends BasePresenter<IGiftView> {
    private int mPages;
    private int progress;
    private boolean isUnSubscribe;
    private GiftModel mGiftModel;
    private ArrayList<GiftBean> girls = new ArrayList<>();

    public GiftPresenter(Activity mActivity, IGiftView view) {
        super(mActivity, view);
        mGiftModel = new GiftModeImpl();
    }

    public void fetchNew(final int page) {
        mGiftModel.fetchGiftPage(page, new Observer<GiftResult>() {
            @Override
            public void onError(Throwable e) {
                mIView.hideRefresh();
                KLog.e(e);
                CrashUtils.crashReport(e);
            }

            @Override
            public void onComplete() {
                int _page = page + 1;
                mIView.setNextPage(_page);
                mIView.hideRefresh();
                mIView.showContent();
            }

            @Override
            public void onSubscribe(Disposable d) {

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
        Observer<GiftResult> subscriber = new Observer<GiftResult>() {
            @Override
            public void onError(Throwable e) {
                mIView.hideRefresh();
                KLog.e(e);
                CrashUtils.crashReport(e);
            }

            @Override
            public void onComplete() {
                mIView.hideRefresh();
            }

            @Override
            public void onSubscribe(Disposable d) {

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
        Observer<List<GiftBean>> subscriber = new Observer<List<GiftBean>>() {
            @Override
            public void onError(Throwable e) {
                mIView.hideRefresh();
                KLog.e(e);
                CrashUtils.crashReport(e);
            }

            @Override
            public void onComplete() {
                mIView.disDialog();
                mIView.refillImagesCount(girls);
                if (!isUnSubscribe) {
                    mIView.gotoBrowseActivity(girls);
                } else {
                    isUnSubscribe = false;
                }
            }

            @Override
            public void onSubscribe(Disposable d) {

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
        isUnSubscribe = true;
        initProgress();
    }

    public void initProgress() {
        progress = 0;
        mIView.setMax(progress);
        mIView.setProgress(progress);
    }

}
