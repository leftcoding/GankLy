package com.gank.gankly.view;

import com.gank.gankly.bean.ResultsBean;

import java.util.List;

/**
 * Create by LingYan on 2016-05-24
 */
public interface IIosView<T extends ResultsBean> extends ISwipeRefreshView {
    void refillDate(List<T> list);

    void appendMoreDate(List<T> list);
}
