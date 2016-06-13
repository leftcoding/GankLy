package com.gank.gankly.ui.view;

import com.gank.gankly.bean.GiftBean;

import java.util.List;

/**
 * Create by LingYan on 2016-06-13
 */
public interface IGiftView extends ISwipeRefreshView {
    void refillDate(List<GiftBean> list);
}
