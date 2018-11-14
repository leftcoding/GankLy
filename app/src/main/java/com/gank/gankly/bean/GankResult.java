package com.gank.gankly.bean;

import android.ly.business.domain.Gank;

import com.gank.gankly.utils.ListUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Create by LingYan on 2016-04-06
 */
public class GankResult extends BaseResult implements Serializable {

    private List<Gank> results;

    public GankResult(List<Gank> results) {
        this.results = results;
    }

    public List<Gank> getResults() {
        return results;
    }

    public boolean isEmpty() {
        return ListUtils.isEmpty(results);
    }

    public int getSize() {
        return ListUtils.getSize(results);
    }
}
