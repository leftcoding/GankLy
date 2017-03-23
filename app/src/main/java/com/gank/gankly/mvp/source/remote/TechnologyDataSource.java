package com.gank.gankly.mvp.source.remote;

import android.support.annotation.Nullable;

import com.gank.gankly.mvp.source.BaseDataSourceModel;

import org.jsoup.nodes.Document;

import io.reactivex.Observable;

/**
 * Create by LingYan on 2016-11-23
 * Email:1373878q.com
 */

public class TechnologyDataSource extends BaseDataSourceModel {
    private static final String BASE_URL = "http://gank.io/xiandu/wow/page/";

    @Nullable
    private static TechnologyDataSource mInstance = null;

    public static TechnologyDataSource getInstance() {
        if (mInstance == null) {
            synchronized (TechnologyDataSource.class) {
                if (mInstance == null) {
                    mInstance = new TechnologyDataSource();
                }
            }
        }
        return mInstance;
    }

    public Observable<Document> fetchData(int page) {
        String url = BASE_URL + page;
        return toObservable(jsoupUrlData(url));
    }
}
