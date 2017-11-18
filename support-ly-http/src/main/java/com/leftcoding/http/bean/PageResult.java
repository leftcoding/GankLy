package com.leftcoding.http.bean;

/**
 * Create by LingYan on 2017-10-12
 */

public class PageResult<T> extends ListResult<T> {
    public int mNextPage;

    public boolean hasNoMore(int limit) {
        if (results != null && !results.isEmpty()) {
            return results.size() < limit;
        }
        return false;
    }
}
