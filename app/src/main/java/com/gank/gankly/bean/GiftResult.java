package com.gank.gankly.bean;

import android.ly.business.domain.Gift;

import java.util.List;

/**
 * Create by LingYan on 2016-05-18
 */
public class GiftResult {
    private int num;
    private List<Gift> mList;

    public GiftResult(int num, List<Gift> list) {
        this.num = num;
        mList = list;
    }
}
