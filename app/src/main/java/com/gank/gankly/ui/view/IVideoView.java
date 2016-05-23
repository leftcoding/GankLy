package com.gank.gankly.ui.view;

import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.config.ViewStatus;

import java.util.List;

/**
 * Create by LingYan on 2016-05-23
 */
public interface IVideoView<T extends ResultsBean> extends ISwipeRefreshView {
    void refillDate(List<T> list);

    void appendMoreDate(List<T> list);

    void hasNoMoreDate();

    ViewStatus getCurViewStatus();
}
