package com.gank.gankly.config;

import android.ly.business.domain.Gank;

import com.gank.gankly.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by LingYan on 2016-04-21
 */
public class MeiziArrayList {
    private static MeiziArrayList sMeiziArrayList;
    private List<Gank> mOneItemsList;
    private List<Gank> mMeiziList;
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

    public void refillOneItems(List<Gank> list) {
        if (ListUtils.isEmpty(mOneItemsList)) {
            mOneItemsList.addAll(list);
        }
    }

    public void addImages(List<Gank> list, int page) {
        if (mPage < page) {
            mMeiziList.addAll(list);
            mPage = page;
        }
    }

    public List<Gank> getImagesList() {
        return mMeiziList;
    }

    public List<Gank> getOneItemsList() {
        return mOneItemsList;
    }

    public boolean isOneItemsEmpty() {
        return ListUtils.getSize(mOneItemsList) <= 0;
    }

    public int getPage() {
        return mPage;
    }

    public Gank getResultBean(int position) {
        if (ListUtils.isEmpty(mMeiziList)) {
            return null;
        }
        return mMeiziList.get(position);
    }
}
