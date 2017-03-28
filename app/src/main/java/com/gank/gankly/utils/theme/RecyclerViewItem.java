package com.gank.gankly.utils.theme;

public class RecyclerViewItem {
    int id;
    int resId;

    public RecyclerViewItem(int id, int resId) {
        this.id = id;
        this.resId = resId;
    }

    public int getId() {
        return id;
    }

    public int getResId() {
        return resId;
    }
}