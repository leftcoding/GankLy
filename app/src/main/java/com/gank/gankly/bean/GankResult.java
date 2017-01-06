package com.gank.gankly.bean;

import com.gank.gankly.utils.ListUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Create by LingYan on 2016-04-06
 */
public class GankResult extends BaseResult implements Serializable {

    private List<ResultsBean> results;

    public GankResult(List<ResultsBean> results) {
        this.results = results;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public boolean isEmpty() {
        return ListUtils.isListEmpty(results);
    }

    public int getSize() {
        return ListUtils.getListSize(results);
    }
}
