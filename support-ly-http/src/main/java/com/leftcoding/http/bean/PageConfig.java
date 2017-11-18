package com.leftcoding.http.bean;

/**
 * Create by LingYan on 2017-11-18
 * Email:137387869@qq.com
 */

public class PageConfig {
    /**
     * 分页个数 默认20
     */
    public int mLimit = 20;

    /**
     * 初始化请求页数
     */
    public int mInitPage = 1;

    /**
     * 当前请求页数
     */
    public int mCurPage = 1;

    public PageConfig() {

    }

    public PageConfig(int limit, int initPage, int curPage) {
        mLimit = limit;
        mInitPage = initPage;
        mCurPage = curPage;
    }

    public boolean isFirstRequest() {
        return mCurPage == mInitPage;
    }
}
