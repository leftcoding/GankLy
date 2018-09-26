package com.gank.gankly.ui.base.adapter;

/**
 * Create by LingYan on 2018-09-26
 */
public abstract class BaseViewManager extends ViewManager {
    protected BaseAdapter baseAdapter;

    @Override
    public void setAdapter(BaseAdapter baseAdapter) {
        this.baseAdapter = baseAdapter;
    }

    @Override
    public void notifyDataSetChanged() {
        baseAdapter.setViewModels(getModels());
        baseAdapter.notifyDataSetChanged();
    }
}
