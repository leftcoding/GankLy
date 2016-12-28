package com.gank.gankly.mvp.source;

import com.gank.gankly.utils.CrashUtils;
import com.socks.library.KLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by LingYan on 2016-10-26
 * Email:137387869@qq.com
 */

public class BaseDataSourceModel {
    protected static final int TIME_OUT = 50 * 1000;
    protected static final String USERAGENT = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.76 Mobile Safari/537.36";
    protected static final String DESKTOP_USERAGENT = "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; Desktop) AppleWebKit/534.13 (KHTML, like Gecko) UCBrowser/8.9.0.25";

    protected <T> Observable<T> toObservable(Observable<T> o) {
        return o.retry(3)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Document> jsoupUrlData(final String url) {
        return Observable.create(new ObservableOnSubscribe<Document>() {
            @Override
            public void subscribe(ObservableEmitter<Document> subscriber) throws Exception {
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
                subscriber.onComplete();
            }
        });
    }

    protected <T> Flowable<T> toObservable(Flowable<T> o) {
        return o.retry(3)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
