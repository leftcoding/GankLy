package com.gank.gankly.mvp.source.remote;

import android.support.annotation.Nullable;

import com.gank.gankly.mvp.source.BaseDataSourceModel;
import com.gank.gankly.utils.CrashUtils;
import com.socks.library.KLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

/**
 * Create by LingYan on 2016-10-26
 * Email:137387869@qq.com
 */

public class MeiziDataSource extends BaseDataSourceModel {
    @Nullable
    private static MeiziDataSource mInstance = null;

    public static MeiziDataSource getInstance() {
        if (mInstance == null) {
            synchronized (MeiziDataSource.class) {
                if (mInstance == null) {
                    mInstance = new MeiziDataSource();
                }
            }
        }
        return mInstance;
    }

    /**
     * 清纯妹子页面数据
     */
    public Observable<Document> fetchPure(final String url) {
        return toObservable(Observable.create((ObservableOnSubscribe<Document>) subscriber -> {
            try {
                Document doc = Jsoup.connect(url)
                        .userAgent(DESKTOP_USERAGENT)
                        .timeout(TIME_OUT)
                        .get();
                subscriber.onNext(doc);
            } catch (IOException e) {
                KLog.e(e);
                CrashUtils.crashReport(e);
            }
            subscriber.onComplete();
        }));
    }

    /**
     * 获取每日更新妹子天数
     */
    public Observable<Document> fetchDaily(final String url) {
        return toObservable(Observable.create((ObservableOnSubscribe<Document>) subscriber -> {
            try {
                Document doc = Jsoup.connect(url)
                        .ignoreContentType(true)
                        .userAgent(USERAGENT)
                        .timeout(TIME_OUT)
                        .get();
                subscriber.onNext(doc);
            } catch (IOException e) {
                KLog.e(e);
                CrashUtils.crashReport(e);
            }
            subscriber.onComplete();
        }));
    }

    /**
     * 获取每日更新妹子当天所有图片
     *
     * @param url 每日更新页面的连接
     */
    public Observable<Document> fetchDailyDays(final String url) {
        return toObservable(Observable.create((ObservableOnSubscribe<Document>) subscriber -> {
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
            subscriber.onComplete();
        }));
    }

    public Observable<Document> fetchDailyDetailUrls(final String imageUrl) {
        return toObservable(Observable.create((ObservableOnSubscribe<Document>) subscribe -> {
            Document doc = null;
            try {
                doc = Jsoup.connect(imageUrl)
                        .userAgent(USERAGENT)
                        .timeout(TIME_OUT)
                        .get();
            } catch (IOException e) {
                KLog.e(e);
                CrashUtils.crashReport(e);
            }
            subscribe.onNext(doc);
            subscribe.onComplete();
        }));
    }
}

