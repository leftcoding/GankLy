package com.gank.gankly.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.gank.gankly.utils.ListUtils;
import com.leftcoding.http.bean.ResultsBean;

import java.util.List;

/**
 * Create by LingYan on 2016-04-06
 */
public class GankResult extends BaseResult implements Parcelable {

    private List<ResultsBean> results;

    public GankResult(List<ResultsBean> results) {
        this.results = results;
    }

    protected GankResult(Parcel in) {
        results = in.createTypedArrayList(ResultsBean.CREATOR);
    }

    public static final Creator<GankResult> CREATOR = new Creator<GankResult>() {
        @Override
        public GankResult createFromParcel(Parcel in) {
            return new GankResult(in);
        }

        @Override
        public GankResult[] newArray(int size) {
            return new GankResult[size];
        }
    };

    public List<ResultsBean> getResults() {
        return results;
    }

    public boolean isEmpty() {
        return ListUtils.isListEmpty(results);
    }

    public int getSize() {
        return ListUtils.getSize(results);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(results);
    }
}
