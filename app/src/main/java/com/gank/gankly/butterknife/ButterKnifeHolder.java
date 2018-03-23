package com.gank.gankly.butterknife;

import android.lectcoding.ui.adapter.BasicHolder;
import android.lectcoding.ui.adapter.ViewItem;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Create by LingYan on 2017-10-13
 */

public abstract class ButterKnifeHolder<II extends ViewItem> extends BasicHolder<II> {
    public ButterKnifeHolder(ViewGroup parent, @LayoutRes int layoutRes) {
        super(parent, layoutRes);
        ButterKnife.bind(this, itemView);
    }
}
