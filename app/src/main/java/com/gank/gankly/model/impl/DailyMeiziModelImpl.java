package com.gank.gankly.model.impl;

import android.text.TextUtils;

import com.gank.gankly.bean.DailyMeiziBean;
import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.model.BaseMeziModel;
import com.gank.gankly.model.DailyMeiziModel;
import com.socks.library.KLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Create by LingYan on 2016-07-05
 */
public class DailyMeiziModelImpl extends BaseMeziModel implements DailyMeiziModel {
    private Subscription mSubscription;
    private boolean isUnSubscribe;

    public DailyMeiziModelImpl() {

    }

    @Override
    public void fetchDailyMeizi(Subscriber<List<DailyMeiziBean>> subscriber) {
        Observable<List<DailyMeiziBean>> observable = Observable.create(new Observable.OnSubscribe<List<DailyMeiziBean>>() {
            @Override
            public void call(Subscriber<? super List<DailyMeiziBean>> subscriber) {
                try {
                    String _url = "http://www.mzitu.com/all";
                    Document doc = Jsoup.connect(_url)
                            .userAgent(USERAGENT)
                            .timeout(timeout)
                            .get();
                    subscriber.onNext(getMonthList(doc));
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
    public void fetchImageUrls(final String url, Subscriber<List<GiftBean>> subscriber) {
        Observable<List<GiftBean>> observable = Observable.create(new Observable.OnSubscribe<List<GiftBean>>() {
            @Override
            public void call(Subscriber<? super List<GiftBean>> subscriber) {
                try {
                    String _url = url;
                    Document doc = Jsoup.connect(_url)
                            .userAgent(USERAGENT)
                            .timeout(timeout)
                            .get();
                    int max = getImageUrlsMax(doc);
                    List<GiftBean> list = getImageUrls(url, max);
                    subscriber.onNext(list);
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
    public void fetchImageList(List<GiftBean> list, Subscriber<List<GiftBean>> _subscriber) {
        KLog.d("list:" + list.size());
        mSubscription = Observable.from(list).flatMap(new Func1<GiftBean, Observable<List<GiftBean>>>() {
            @Override
            public Observable<List<GiftBean>> call(GiftBean giftBean) {
                final String url = giftBean.getImgUrl();
//                iGiftView.setProgress(progress++);
                return Observable.create(new Observable.OnSubscribe<List<GiftBean>>() {
                    @Override
                    public void call(Subscriber<? super List<GiftBean>> subscriber) {
                        try {
                            if (!isUnSubscribe) {
                                Document doc = Jsoup.connect(url)
                                        .userAgent(USERAGENT)
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
                .subscribe(_subscriber);
    }

    private int getImageUrlsMax(Document doc) {
        if (doc != null) {
            Elements page = doc.select(".prev-next-page");
            if (page.size() > 0) {
                String mm = page.get(0).text();
                String[] split = mm.split("/");
                int length = split.length;
                if (length > 1) {
                    String s = split[1];
                    mm = s.substring(0, s.length() - 1);
                    try {
                        return Integer.parseInt(mm);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return 0;
    }

    private List<GiftBean> getImageCountList(Document doc) {
        List<GiftBean> list = new ArrayList<>();
        if (doc != null) {
            Elements page = doc.select("#content a img");
            for (int i = 0; i < page.size(); i++) {
                String imgUrl = page.get(i).attr("src");
                list.add(new GiftBean(imgUrl));
            }
        }
        return list;
    }

    private List<GiftBean> getImageUrls(String url, int max) {
        List<GiftBean> list = new ArrayList<>();
        if (!TextUtils.isEmpty(url) && max > 0) {
            for (int i = 1; i <= max; i++) {
                String u = url + "/" + i;
                list.add(new GiftBean(u));
            }
        }
        return list;
    }


    private List<DailyMeiziBean> getMonthList(Document doc) {
        List<DailyMeiziBean> list = new ArrayList<>();
        if (doc != null) {
            Elements times = doc.select(".post-content .archive-brick");
            Elements ahref = doc.select(".post-content .archive-brick a");
            for (int i = 0; i < ahref.size(); i++) {
                list.add(new DailyMeiziBean(ahref.get(i).attr("href"), times.get(i).text()));
            }
        }
        return list;
    }


}
