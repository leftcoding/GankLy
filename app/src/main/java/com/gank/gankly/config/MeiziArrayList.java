package com.gank.gankly.config;

import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.utils.ListUtils;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by LingYan on 2016-04-21
 */
public class MeiziArrayList {
    private static MeiziArrayList sMeiziArrayList;
    private List<ResultsBean> mArrayList;
    private List<ResultsBean> mMeiziList;
    private int mPage = 0;

    private MeiziArrayList() {
        mArrayList = new ArrayList<>();
        mMeiziList = new ArrayList<>();
    }

    public static MeiziArrayList getInstance() {
        if (sMeiziArrayList == null) {
            sMeiziArrayList = new MeiziArrayList();
        }
        return sMeiziArrayList;
    }

    public void addBeanAndPage(List<ResultsBean> list, int page) {
        KLog.d("page:" + page + ",mPage:" + mPage);
        if (mPage < page) {
            KLog.d("mPage < page:" + page);
            mArrayList.addAll(list);
            mPage = page;
        }
        KLog.d("--end--page:" + page);
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

    public ResultsBean getResultBean(int position) {
        if (ListUtils.isListEmpty(mArrayList)) {
            return null;
        }
        return mArrayList.get(position);
    }
}
