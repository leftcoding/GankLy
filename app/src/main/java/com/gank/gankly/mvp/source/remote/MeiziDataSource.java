package com.gank.gankly.mvp.source.remote;

import android.support.annotation.Nullable;

import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.mvp.source.GirlsDataSource;
import com.gank.gankly.utils.CrashUtils;
import com.socks.library.KLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Create by LingYan on 2016-10-26
 * Email:137387869@qq.com
 */

public class MeiziDataSource extends GirlsDataSource {
    private static final String MEIZI_DAILY_URL = "http://m.mzitu.com/all";

    @Nullable
    private static MeiziDataSource INSTANCE = null;

    public static MeiziDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (MeiziDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MeiziDataSource();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 获取每日更新妹子天数
     */
    public Observable<Document> fetchDailyGirls() {
        return toObservable(Observable.create(new Observable.OnSubscribe<Document>() {
            @Override
            public void call(Subscriber<? super Document> subscriber) {
                try {
                    Document doc = Jsoup.connect(MEIZI_DAILY_URL)
                            .ignoreContentType(true)
                            .userAgent(MEIZI_USERAGENT)
                            .timeout(TIME_OUT)
                            .get();
                    subscriber.onNext(doc);
                } catch (IOException e) {
                    KLog.e(e);
                    CrashUtils.crashReport(e);
                }
                subscriber.onCompleted();
            }
        }));
    }

    /**
     * 获取每日更新妹子当天所有图片
     *
     * @param url 每日更新页面的连接
     */
    public Observable<Document> fetchImageUrls(final String url) {
        return toObservable(Observable.create(new Observable.OnSubscribe<Document>() {
            @Override
            public void call(Subscriber<? super Document> subscriber) {
                try {
                    Document doc = Jsoup.connect(url)
                            .userAgent(USERAGENT)
                            .timeout(TIME_OUT)
                            .get();
                    subscriber.onNext(doc);
                } catch (IOException e) {
                    KLog.e(e);
                    CrashUtils.crashReport(e);
                }
                subscriber.onCompleted();
            }
        }));
    }

    public Observable<Document> getImageList(List<GiftBean> list) {
        return toObservable(Observable.from(list).flatMap(new Func1<GiftBean, Observable<Document>>() {
            @Override
            public Observable<Document> call(GiftBean giftBean) {
                final String url = giftBean.getImgUrl();
                return Observable.create(new Observable.OnSubscribe<Document>() {
                    @Override
                    public void call(Subscriber<? super Document> subscriber) {
                        try {
                            Document doc = Jsoup.connect(url)
                                    .userAgent(USERAGENT)
                                    .timeout(TIME_OUT)
                                    .get();
                            subscriber.onNext(doc);
                        } catch (IOException e) {
                            KLog.e(e);
                            CrashUtils.crashReport(e);
                        }
                        subscriber.onCompleted();
                    }
                });
            }
        }));
    }
}

