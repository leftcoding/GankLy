package com.gank.gankly.presenter;

import android.app.Activity;

import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.bean.GiftResult;
import com.gank.gankly.model.GiftModel;
import com.gank.gankly.model.impl.GiftModeImpl;
import com.gank.gankly.view.IGiftView;
import com.socks.library.KLog;

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
                KLog.e(e);
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
        KLog.d("fetchImagePageList");
        initProgress();
        Subscriber<GiftResult> subscriber = new Subscriber<GiftResult>() {
            @Override
            public void onCompleted() {
                mIView.hideRefresh();
            }

            @Override
            public void onError(Throwable e) {
                mIView.hideRefresh();
                KLog.e(e);
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

//    public void fetchImagePages(final String url) {
//        mSubscription = Observable.create(new Observable.OnSubscribe<GiftResult>() {
//            @Override
//            public void call(Subscriber<? super GiftResult> subscriber) {
//                try {
//                    Document doc = Jsoup.connect(url)
//                            .userAgent(DESKTOP_USERAGENT)
//                            .timeout(timeout)
//                            .get();
//                    subscriber.onNext(new GiftResult(0, getImageCount(url, doc)));
//                } catch (Exception e) {
//                    KLog.e(e.toString() + e);
//                }
//                subscriber.onCompleted();
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<GiftResult>() {
//                    @Override
//                    public void onCompleted() {
//                        mIView.hideRefresh();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        mIView.hideRefresh();
//                        KLog.e(e);
//                    }
//
//                    @Override
//                    public void onNext(GiftResult giftResult) {
//                        if (giftResult != null && giftResult.getSize() > 0) {
//                            mIView.setMax(giftResult.getSize());
//                            fetchImages(giftResult.getList());
//                        }
//                    }
//                });
//    }

//    private List<GiftBean> getImageCount(String url, Document doc) {
//        List<GiftBean> list;
//        if (doc == null) {
//            return null;
//        }
//        list = new ArrayList<>();
//        Elements pages = doc.select(".pagenavi a[href]");
//        int size = pages.size();
//        for (int i = size - 1; i > 0; i--) {
//            String page = pages.get(i).text();
//            if (StringUtils.isNumeric(page)) {
//                mDetailsPageCount = Integer.parseInt(page);
//                break;
//            }
//        }
//
//        if (mDetailsPageCount > 0) {
//            for (int i = 1; i <= mDetailsPageCount; i++) {
//                String _url = url + "/" + i;
//                list.add(new GiftBean(_url));
//            }
//        }
//
//        return list;
//    }

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
                KLog.e(e);
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
