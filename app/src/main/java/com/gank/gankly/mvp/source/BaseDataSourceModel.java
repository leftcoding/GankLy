package com.gank.gankly.mvp.source;

import com.gank.gankly.utils.CrashUtils;
import com.socks.library.KLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Create by LingYan on 2016-10-26
 * Email:137387869@qq.com
 */

public class BaseDataSourceModel {
    protected static final int TIME_OUT = 50 * 1000;
    protected static final String USERAGENT = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.76 Mobile Safari/537.36";

    protected <T> Observable<T> toObservable(Observable<T> o) {
        return o.retry(3)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Document> jsoupUrlData(final String url) {
        return Observable.create(new Observable.OnSubscribe<Document>() {
            @Override
            public void call(Subscriber<? super Document> subscriber) {
                Document doc = null;
                try {
                    doc = Jsoup.connect(url)
                            .userAgent(USERAGENT)
                            .timeout(TIME_OUT)
                            .get();
                } catch (IOException e) {
                    KLog.e(e);
                    CrashUtils.crashReport(e);
                }
                subscriber.onNext(doc);
                subscriber.onCompleted();
            }
        });
    }
}
