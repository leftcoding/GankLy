package com.gank.gankly.mvp.observer;

/**
 * Create by LingYan on 2017-10-12
 */

public interface ILoadMoreException {
    void refreshError(String str);

    void appendError(String str);
}
