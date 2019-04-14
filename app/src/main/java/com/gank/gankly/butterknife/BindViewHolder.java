package com.gank.gankly.butterknife;

import androidx.annotation.LayoutRes;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Create by LingYan on 2017-10-13
 */

public abstract class BindViewHolder<II extends ItemModel> extends BasicHolder<II> {
    public BindViewHolder(ViewGroup parent, @LayoutRes int layout) {
        super(parent, layout);
        ButterKnife.bind(this, itemView);
    }
}
