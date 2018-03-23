package com.gank.gankly.mvp.source.remote;

/**
 * Create by LingYan on 2016-11-23
 */

public class TeamBlogDataSource  {
    private static final String BASE_URL = "http://gank.io/xiandu/teamblog/page/";

    private volatile static TeamBlogDataSource mInstance;

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

}
