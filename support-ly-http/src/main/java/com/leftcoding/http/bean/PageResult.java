package com.leftcoding.http.bean;

/**
 * Create by LingYan on 2017-10-12
 */

public class PageResult<T> extends ListResult<T> {
    public int nextPage;

    public int curPage;

    public boolean hasNoMore;
}
