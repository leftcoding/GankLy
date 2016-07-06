package com.gank.gankly.presenter;

import com.gank.gankly.bean.GiftBean;

import java.util.List;

/**
 * Create by LingYan on 2016-07-06
 */
public interface DailyMeiziPresenter extends RefreshPresenter {
    void fetchImageUrls(String url);

    List<GiftBean> getList();
}
