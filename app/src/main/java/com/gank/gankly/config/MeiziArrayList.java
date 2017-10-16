package com.gank.gankly.config;

import com.gank.gankly.utils.ListUtils;
import com.leftcoding.http.bean.ResultsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by LingYan on 2016-04-21
 */
public class MeiziArrayList {
    private static MeiziArrayList sMeiziArrayList;
    private List<ResultsBean> mOneItemsList;
    private List<ResultsBean> mMeiziList;
    private int mPage = 0;

    private MeiziArrayList() {
        mOneItemsList = new ArrayList<>();
        mMeiziList = new ArrayList<>();
    }

    public static MeiziArrayList getInstance() {
        if (sMeiziArrayList == null) {
            sMeiziArrayList = new MeiziArrayList();
        }
        return sMeiziArrayList;
    }

    public void refillOneItems(List<ResultsBean> list) {
        if (ListUtils.isListEmpty(mOneItemsList)) {
            mOneItemsList.addAll(list);
        }
    }

    public void addImages(List<ResultsBean> list, int page) {
        if (mPage < page) {
            mMeiziList.addAll(list);
            mPage = page;
        }
    }

    public List<ResultsBean> getImagesList() {
        return mMeiziList;
    }

    public List<ResultsBean> getOneItemsList() {
        return mOneItemsList;
    }

    public boolean isOneItemsEmpty() {
        return ListUtils.getSize(mOneItemsList) <= 0;
    }

    public int getPage() {
        return mPage;
    }

    public ResultsBean getResultBean(int position) {
        if (ListUtils.isListEmpty(mMeiziList)) {
            return null;
        }
        return mMeiziList.get(position);
    }
}
