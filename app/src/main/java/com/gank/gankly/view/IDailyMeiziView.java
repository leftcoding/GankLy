package com.gank.gankly.view;

import java.util.List;

/**
 * Create by LingYan on 2016-07-05
 */
public interface IDailyMeiziView<P> extends ISwipeRefreshView {
    void refillDate(List<P> list);

    void gotoBrowseActivity();

    void disDialog();
}
