package com.gank.gankly.presenter;

import android.app.Activity;

import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.bean.GiftResult;
import com.gank.gankly.model.GiftModel;
import com.gank.gankly.model.impl.GiftModeImpl;
import com.gank.gankly.view.IGiftView;
import com.socks.library.KLog;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Create by LingYan on 2016-06-13
 */
public class GiftPresenter extends BasePresenter<IGiftView> {
    private static final int timeout = 50 * 1000;
    private static final String DESKTOP_USERAGENT = "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; Desktop) AppleWebKit/534.13 (KHTML, like Gecko) UCBrowser/8.9.0.25";
    private String url = "http://www.mzitu.com/mm/page/";
    private int mPages;
    private Subscription mSubscription;
    private int mDetailsPageCount = 1;
    private int progress;
    private boolean isUnSubscribe;
    private GiftModel mGiftModel;
    private String mCurUrl;

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

//    new Subscriber<GiftResult>() {
//        @Override
//        public void onCompleted() {
//            listener.onCompleted();
//        }
//
//        @Override
//        public void onError(Throwable e) {
//            listener.onError(e);
//        }
//
//        @Override
//        public void onNext(GiftResult giftResult) {
//            if (giftResult != null && giftResult.getSize() > 0) {
//                listener.setMaxValue(maxImagePageNum);
//            }
//        }
//    }

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
        KLog.d("fetchImagesList");
        isUnSubscribe = false;
        mGiftModel.setIsUnSubscribe(isUnSubscribe);
        Subscriber<List<GiftBean>> subscriber = new Subscriber<List<GiftBean>>() {
            @Override
            public void onCompleted() {
                mIView.disDialog();
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
                    mIView.refillImagesCount(giftResult);
                }
            }
        };
        mGiftModel.fetchImagesList(list, subscriber, mIView);
//        Observable.from(list).flatMap(new Func1<GiftBean, Observable<List<GiftBean>>>() {
//            @Override
//            public Observable<List<GiftBean>> call(GiftBean giftBean) {
//                final String url = giftBean.getImgUrl();
//                mIView.setProgress(progress++);
//                return Observable.create(new Observable.OnSubscribe<List<GiftBean>>() {
//                    @Override
//                    public void call(Subscriber<? super List<GiftBean>> subscriber) {
//                        try {
//                            if (!isUnSubscribe) {
//                                Document doc = Jsoup.connect(url)
//                                        .userAgent(DESKTOP_USERAGENT)
//                                        .timeout(timeout)
//                                        .get();
//                                subscriber.onNext(getImageCountList(doc));
//                            }
//                        } catch (IOException e) {
//                            KLog.e(e);
//                        }
//                        subscriber.onCompleted();
//                    }
//                });
//            }
//        })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<List<GiftBean>>() {
//                    @Override
//                    public void onCompleted() {
//                        mIView.disDialog();
//                        if (!isUnSubscribe) {
//                            mIView.gotoBrowseActivity();
//                        } else {
//                            isUnSubscribe = false;
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        mIView.hideRefresh();
//                        KLog.e(e);
//                    }
//
//                    @Override
//                    public void onNext(List<GiftBean> giftResult) {
//                        if (giftResult != null && giftResult.size() > 0) {
//                            mIView.refillImagesCount(giftResult);
//                        }
//                    }
//                });
    }

//    private List<GiftBean> getImageCountList(Document doc) {
//        List<GiftBean> giftBeen = null;
//        if (doc != null) {
//            giftBeen = new ArrayList<>();
//            Elements links = doc.select(".main-image img[src$=.jpg]");
//            String img = links.get(0).attr("src");
//            giftBeen.add(new GiftBean(img));
//        }
//        return giftBeen;
//    }

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
