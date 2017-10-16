package com.gank.gankly.ui.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Create by LingYan on 2017-10-13
 */

public abstract class ButterHolder extends BasicHolder {
    public ButterHolder(Context context, ViewGroup parent, @LayoutRes int layoutID) {
        super(context, parent, layoutID);
        ButterKnife.bind(this, itemView);
    }
}
