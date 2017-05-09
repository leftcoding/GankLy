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

public class MeiZiTuDataSource extends BaseDataSourceModel {
    @Nullable
    private static MeiZiTuDataSource mInstance = null;

    public static MeiZiTuDataSource getInstance() {
        if (mInstance == null) {
            synchronized (MeiZiTuDataSource.class) {
                if (mInstance == null) {
                    mInstance = new MeiZiTuDataSource();
                }
            }
        }
        return mInstance;
    }

    /**
     * meizitu 清纯照片
     */
    public Observable<Document> fetchQingChun(final String url) {
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
}

