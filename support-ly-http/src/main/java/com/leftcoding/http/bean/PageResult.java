package com.leftcoding.http.bean;

/**
 * Create by LingYan on 2017-10-12
 */

public class PageResult<T> extends ListResult<T> {
    public int nextPage;

    public boolean hasNoMore(int limit) {
        return results != null && !results.isEmpty() && results.size() < limit;
    }
}
