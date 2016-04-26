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
    private int mPage = 0;

    private MeiziArrayList() {
        mArrayList = new ArrayList<>();
    }

    public MeiziArrayList(List<ResultsBean> arrayList, int page) {
        mArrayList = arrayList;
        mPage = page;
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

    public int getPage() {
        return mPage;
    }

    public void setPage(int page) {
        this.mPage = page;
    }

    public ResultsBean getResultBean(int position) {
        if (ListUtils.isListEmpty(mArrayList)) {
            return null;
        }
        return mArrayList.get(position);
    }
}
