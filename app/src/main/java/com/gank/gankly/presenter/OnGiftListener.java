package com.gank.gankly.presenter;

/**
 * Create by LingYan on 2016-06-29
 */
public interface OnGiftListener extends OnFetchListener {
    void setMaxValue(int value);

    void setProgress(int value);

    void disDialog();

    void onImageComplete();

    void onImageEmpty();
}
