package com.gank.gankly.bean;

/**
 * Create by LingYan on 2016-05-27
 */
public class RxCollect {
    private boolean isCollect;

    public RxCollect(boolean isCollect) {
        this.isCollect = isCollect;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }
}
