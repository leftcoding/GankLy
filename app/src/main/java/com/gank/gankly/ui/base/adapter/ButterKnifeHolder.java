package com.gank.gankly.ui.base.adapter;

import android.view.View;

import butterknife.ButterKnife;

/**
 * Create by LingYan on 2018-09-26
 */
public class ButterKnifeHolder extends BaseHolder {
    public ButterKnifeHolder(View view) {
        super(view);
        ButterKnife.bind(this, itemView);
    }
}
