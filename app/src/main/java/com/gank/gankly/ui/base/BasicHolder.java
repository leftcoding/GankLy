package com.gank.gankly.ui.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Create by LingYan on 2017-10-14
 */

public abstract class BasicHolder extends RecyclerView.ViewHolder {

    public BasicHolder(Context context, ViewGroup parent, @LayoutRes int layoutID) {
        super(LayoutInflater.from(context).inflate(layoutID, parent, false));

    }
}
