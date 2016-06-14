package com.gank.gankly.ui.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.bean.GiftResult;
import com.gank.gankly.ui.view.IGiftView;
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

    public GiftPresenter(Activity mActivity, IGiftView view) {
        super(mActivity, view);
    }

    public void fetchPageCount(final int mCurPage) {
        Observable<GiftResult> observable = Observable.create(new Observable.OnSubscribe<GiftResult>() {
            @Override
            public void call(Subscriber<? super GiftResult> subscriber) {
                try {
                    String _url = url + mCurPage;
                    Document doc = Jsoup.connect(_url)
                            .userAgent(DESKTOP_USERAGENT)
                            .timeout(timeout)
                            .get();
                    int num = getBigPageNum(doc);
                    subscriber.onNext(new GiftResult(num, getPageCount(doc)));
                } catch (IOException e) {
                    KLog.e(e);
                }
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Subscriber<GiftResult>() {
            @Override
            public void onCompleted() {
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
                if (giftResult != null) {
                    if (giftResult.getSize() > 0) {
                        if (mCurPage == 1) {
                            mIView.clear();
                        }
                        mIView.refillDate(giftResult.getList());
                    }
                    mPages = giftResult.getNum();
                }
            }
        });
    }

    private int getBigPageNum(Document doc) {
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

    private List<GiftBean> getPageCount(Document doc) {
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

    public void fetchImagePages(final String url) {
        mSubscription = Observable.create(new Observable.OnSubscribe<GiftResult>() {
            @Override
            public void call(Subscriber<? super GiftResult> subscriber) {
                try {
                    Document doc = Jsoup.connect(url)
                            .userAgent(DESKTOP_USERAGENT)
                            .timeout(timeout)
                            .get();
                    subscriber.onNext(new GiftResult(0, getImageCount(url, doc)));
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
                            fetchImages(giftResult.getList());
                        }
                    }
                });
    }

    private List<GiftBean> getImageCount(String url, Document doc) {
        List<GiftBean> list;
        if (doc == null) {
            return null;
        }
        list = new ArrayList<>();
        Elements pages = doc.select(".pagenavi a[href]");
        int size = pages.size();
        for (int i = size - 1; i > 0; i--) {
            String page = pages.get(i).text();
            if (StringUtils.isNumeric(page)) {
                mDetailsPageCount = Integer.parseInt(page);
                break;
            }
        }

        if (mDetailsPageCount > 0) {
            for (int i = 1; i <= mDetailsPageCount; i++) {
                String _url = url + "/" + i;
                list.add(new GiftBean(_url));
            }
        }

        return list;
    }

    private void fetchImages(List<GiftBean> list) {
        Observable.from(list).flatMap(new Func1<GiftBean, Observable<List<GiftBean>>>() {
            @Override
            public Observable<List<GiftBean>> call(GiftBean giftBean) {
                final String url = giftBean.getImgUrl();
                mIView.setProgress(progress++);
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

    public void unSubscribe() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
            isUnSubscribe = true;
            initProgress();
        }
    }

    public void initProgress() {
        progress = 0;
        mIView.setMax(progress);
        mIView.setProgress(progress);
    }

    public int getPages() {
        return mPages;
    }
}
