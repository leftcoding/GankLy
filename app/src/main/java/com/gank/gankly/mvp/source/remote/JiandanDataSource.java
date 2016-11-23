package com.gank.gankly.mvp.source.remote;

import android.support.annotation.Nullable;

import com.gank.gankly.mvp.source.BaseDataSourceModel;
import com.gank.gankly.utils.CrashUtils;
import com.socks.library.KLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;

/**
 * Create by LingYan on 2016-11-21
 * Email:137387869@qq.com
 */

public class JiandanDataSource extends BaseDataSourceModel {

    @Nullable
    private static JiandanDataSource INSTANCE = null;

    public static JiandanDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (JiandanDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new JiandanDataSource();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 获取煎蛋数据网址
     *
     * @param url 请求地址
     * @return
     */
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
