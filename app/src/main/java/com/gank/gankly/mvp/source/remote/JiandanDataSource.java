package com.gank.gankly.mvp.source.remote;

import android.support.annotation.Nullable;

import com.gank.gankly.mvp.source.BaseDataSourceModel;
import com.gank.gankly.utils.CrashUtils;
import com.socks.library.KLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Create by LingYan on 2016-11-21
 * Email:137387869@qq.com
 */

public class JiandanDataSource extends BaseDataSourceModel {

    @Nullable
    private static JiandanDataSource mInstance = null;

    public static JiandanDataSource getInstance() {
        if (mInstance == null) {
            synchronized (JiandanDataSource.class) {
                if (mInstance == null) {
                    mInstance = new JiandanDataSource();
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取煎蛋数据网址
     *
     * @param url 请求地址
     * @return
     */
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
}
