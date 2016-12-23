package com.gank.gankly.model;

import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.bean.GiftResult;
import com.gank.gankly.view.IGiftView;

import java.util.List;

import io.reactivex.Observer;

/**
 * Create by LingYan on 2016-06-29
 */
public interface GiftModel {
    void fetchGiftPage(int page, Observer<GiftResult> subscriber);

    void fetchImagesPageList(String url, Observer<GiftResult> subscription);

    void fetchImagesList(List<GiftBean> list, Observer<List<GiftBean>> subscriber, IGiftView iGiftView);

    void setIsUnSubscribe(boolean isUnSubscribe);
}
