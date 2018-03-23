package com.gank.gankly.mvp.source.remote;

/**
 * Create by LingYan on 2016-11-23
 */

public class TechnologyDataSource {
    private static final String BASE_URL = "http://gank.io/xiandu/wow/page/";

    private volatile static TechnologyDataSource mInstance;

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
}
