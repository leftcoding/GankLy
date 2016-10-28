package com.gank.gankly.view;

import com.gank.gankly.bean.GiftBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by LingYan on 2016-06-13
 */
public interface IGiftView extends ISwipeRefreshView {
    void refillDate(List<GiftBean> list);

    void refillImagesCount(List<GiftBean> giftResult);

    void gotoBrowseActivity(ArrayList<GiftBean> list);

    void setMax(int max);

    void setProgress(int value);

    void disDialog();

    void setNextPage(int page);

}
