package com.gank.gankly.bean;

import com.gank.gankly.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by LingYan on 2016-04-21
 */
public class MeiziArrayList {
    private static MeiziArrayList sMeiziArrayList;
    private List<ResultsBean> mArrayList;

    private MeiziArrayList() {
        mArrayList = new ArrayList<>();
    }

    public static MeiziArrayList getInstance() {
        if (sMeiziArrayList == null) {
            sMeiziArrayList = new MeiziArrayList();
        }
        return sMeiziArrayList;
    }

    public void addAll(List<ResultsBean> list) {
        mArrayList.addAll(list);
    }

    public void remove(int position) {
        if (ListUtils.isListEmpty(mArrayList)) return;
        mArrayList.remove(position);
    }

    public void clear() {
        mArrayList.clear();
    }

    public int size() {
        return mArrayList.size();
    }

    public List<ResultsBean> getArrayList() {
        return mArrayList;
    }

    public ResultsBean getResultBean(int position) {
        if (ListUtils.isListEmpty(mArrayList)) {
            return null;
        }
        return mArrayList.get(position);
    }
}
