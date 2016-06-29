package com.gank.gankly.model.impl;

import android.text.TextUtils;

import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.bean.GiftResult;
import com.gank.gankly.model.GiftModel;
import com.gank.gankly.presenter.OnFetchListener;
import com.gank.gankly.presenter.OnGiftListener;
import com.gank.gankly.utils.StringUtils;
import com.socks.library.KLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Create by LingYan on 2016-06-29
 */
public class GiftModeImpl implements GiftModel {
    private static final int timeout = 50 * 1000;
    private static final String DESKTOP_USERAGENT = "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; Desktop) AppleWebKit/534.13 (KHTML, like Gecko) UCBrowser/8.9.0.25";
    private String url = "http://www.mzitu.com/mm/page/";

    private OnFetchListener<GiftBean> mListener;
    private OnGiftListener mGiftListener;

    private int mMaxPageNumber;
    private int maxImagePageNum;
    private Subscription mSubscription;
    private boolean isUnSubscribe;
    private int progress;

    public GiftModeImpl() {
    }

    @Override
    public void fetchGift(final int page,Subscriber<GiftResult> subscriber) {
        Observable<GiftResult> observable = Observable.create(new Observable.OnSubscribe<GiftResult>() {
            @Override
            public void call(Subscriber<? super GiftResult> subscriber) {
                try {
                    String _url = url + page;
                    Document doc = Jsoup.connect(_url)
                            .userAgent(DESKTOP_USERAGENT)
                            .timeout(timeout)
                            .get();
                    mMaxPageNumber = getMaxPageNum(doc);
                    subscriber.onNext(new GiftResult(mMaxPageNumber, getPageLists(doc)));
                } catch (IOException e) {
                    KLog.e(e);
                }
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(subscriber);
    }

    @Override
    public int getMaxPageNumber() {
        return mMaxPageNumber;
    }

    private int getMaxPageNum(Document doc) {
        int p = 0;
        if (doc != null) {
            Elements count = doc.select(".nav-links a[href]");
            int size = count.size();
            if (size > 0) {
                for (int i = size - 1; i >= 0; i--) {
                    String num = count.get(i).text();
                    if (StringUtils.isNumeric(num)) {
                        try {
                            return Integer.parseInt(num);
                        } catch (IllegalFormatException e) {
                            KLog.e(e);
                        }
                    }
                }
            }
        }
        return p;
    }

    private List<GiftBean> getPageLists(Document doc) {
        List<GiftBean> list = null;
        if (doc == null) {
            return null;
        }
        Elements hrefs = doc.select("#pins > li > a");
        Elements img = doc.select("#pins a img");
        Elements times = doc.select(".time");
        Elements views = doc.select(".view");

        int countSize = hrefs.size();
        int imgSize = img.size();
        int size = countSize > imgSize ? imgSize : countSize;

        if (size > 0) {
            list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                String imgUrl = img.get(i).attr("data-original");
                String title = img.get(i).attr("alt");
                String url = hrefs.get(i).attr("href");
                String time = times.get(i).text();
                String view = views.get(i).text();
                if (!TextUtils.isEmpty(imgUrl) && !TextUtils.isEmpty(url)) {
                    list.add(new GiftBean(imgUrl, url, time, view, title));
                }
            }
        }
        return list;
    }

    @Override
    public void fetchImagesList(final String url, final OnGiftListener listener) {
        mGiftListener = listener;
        mSubscription = Observable.create(new Observable.OnSubscribe<GiftResult>() {
            @Override
            public void call(Subscriber<? super GiftResult> subscriber) {
                try {
                    Document doc = Jsoup.connect(url)
                            .userAgent(DESKTOP_USERAGENT)
                            .timeout(timeout)
                            .get();
                    subscriber.onNext(new GiftResult(0, getImageUrls(url, doc)));
                } catch (Exception e) {
                    KLog.e(e.toString() + e);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GiftResult>() {
                    @Override
                    public void onCompleted() {
                        listener.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(GiftResult giftResult) {
                        if (giftResult != null && giftResult.getSize() > 0) {
                            listener.setMaxValue(maxImagePageNum);
                        }
                    }
                });
    }

    private List<GiftBean> getImageUrls(String url, Document doc) {
        List<GiftBean> list;
        if (doc == null) {
            return null;
        }
        list = new ArrayList<>();
        Elements pages = doc.select(".pagenavi a[href]");
        int size = pages.size();
        int maxImagePage = 0;
        for (int i = size - 1; i > 0; i--) {
            String page = pages.get(i).text();
            if (StringUtils.isNumeric(page)) {
                maxImagePage = Integer.parseInt(page);
                break;
            }
        }

        if (maxImagePage > 0) {
            maxImagePageNum = maxImagePage;
            for (int i = 1; i <= maxImagePage; i++) {
                String _url = url + "/" + i;
                list.add(new GiftBean(_url));
            }
        }

        return list;
    }

    private void fetchImagesList(List<GiftBean> list) {
        Observable.from(list).flatMap(new Func1<GiftBean, Observable<List<GiftBean>>>() {
            @Override
            public Observable<List<GiftBean>> call(GiftBean giftBean) {
                final String url = giftBean.getImgUrl();
                mGiftListener.setProgress(progress++);
                return Observable.create(new Observable.OnSubscribe<List<GiftBean>>() {
                    @Override
                    public void call(Subscriber<? super List<GiftBean>> subscriber) {
                        try {
                            if (!isUnSubscribe) {
                                Document doc = Jsoup.connect(url)
                                        .userAgent(DESKTOP_USERAGENT)
                                        .timeout(timeout)
                                        .get();
                                subscriber.onNext(getImageCountList(doc));
                            }
                        } catch (IOException e) {
                            KLog.e(e);
                        }
                        subscriber.onCompleted();
                    }
                });
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<GiftBean>>() {
                    @Override
                    public void onCompleted() {
                        mGiftListener.disDialog();
                        if (!isUnSubscribe) {
                            mGiftListener.onCompleted();
                        } else {
                            isUnSubscribe = false;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                        mGiftListener.onError(e);
                    }

                    @Override
                    public void onNext(List<GiftBean> giftResult) {
                        if (giftResult != null && giftResult.size() > 0) {
                            mGiftListener.onImageComplete();
                        } else {
                            mGiftListener.onImageEmpty();
                        }
                    }
                });
    }

    private List<GiftBean> getImageCountList(Document doc) {
        List<GiftBean> giftBeen = null;
        if (doc != null) {
            giftBeen = new ArrayList<>();
            Elements links = doc.select(".main-image img[src$=.jpg]");
            String img = links.get(0).attr("src");
            giftBeen.add(new GiftBean(img));
        }
        return giftBeen;
    }

}
