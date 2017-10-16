package com.gank.gankly.bean;

import com.gank.gankly.utils.ListUtils;

import java.util.List;

/**
 * Create by LingYan on 2016-05-18
 */
public class GiftResult {
    private int num;
    private List<GiftBean> mList;

    public GiftResult(int num, List<GiftBean> list) {
        this.num = num;
        mList = list;
    }

    public List<GiftBean> getList() {
        return mList;
    }

    public void setList(List<GiftBean> list) {
        mList = list;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getSize() {
        return ListUtils.getSize(mList);
    }
}
