package com.gank.gankly.mvp.source.remote;

import android.support.annotation.Nullable;

import com.gank.gankly.mvp.source.BaseDataSourceModel;

import org.jsoup.nodes.Document;

import io.reactivex.Observable;

/**
 * Create by LingYan on 2016-11-23
 * Email:1373878q.com
 */

public class TeamBlogDataSource extends BaseDataSourceModel {
    private static final String BASE_URL = "http://gank.io/xiandu/teamblog/page/";

    @Nullable
    private static TeamBlogDataSource mInstance = null;

    public static TeamBlogDataSource getInstance() {
        if (mInstance == null) {
            synchronized (TeamBlogDataSource.class) {
                if (mInstance == null) {
                    mInstance = new TeamBlogDataSource();
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
