package com.gank.gankly.model;

import io.reactivex.Observer;

/**
 * Create by LingYan on 2016-07-13
 * Email:137387869@qq.com
 */
public interface BaseModel {
    void fetchData(int page, int limit, Observer subscriber);
}
