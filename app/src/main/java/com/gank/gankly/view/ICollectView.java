package com.gank.gankly.view;

import com.gank.gankly.data.entity.UrlCollect;

import java.util.List;

/**
 * Create by LingYan on 2016-05-12
 */
public interface ICollectView<T extends UrlCollect> extends ISwipeRefreshView {

    void refillDate(List<T> list);

    void appendMoreDate(List<T> list);

    void hasNoMoreDate();

    void fetchFinish();
}
