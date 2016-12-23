package com.gank.gankly.model;

import io.reactivex.Observer;

/**
 * Create by LingYan on 2016-07-20
 * Email:137387869@qq.com
 */
public interface JiandanModel {
    void fetchData(int page, Observer s);
}
