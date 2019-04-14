package com.gank.gankly.mvp.subseribe;

import android.ly.business.domain.Gank;
import android.ly.business.domain.PageEntity;

import com.gank.gankly.mvp.observer.RefreshOnObserver;

/**
 * Create by LingYan on 2018-03-22
 */

public class PageSubscribe extends RefreshOnObserver<PageEntity<Gank>> {
    public PageSubscribe(String requestTag) {
        super(requestTag);
    }

    @Override
    protected void onSuccess(PageEntity<Gank> gankPageEntity) {

    }

    @Override
    protected void onFailure() {

    }
}