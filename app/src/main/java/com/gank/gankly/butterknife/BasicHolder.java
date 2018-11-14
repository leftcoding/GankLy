package com.gank.gankly.butterknife;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public abstract class BasicHolder<II extends ItemModel> extends RecyclerView.ViewHolder {
    public BasicHolder(ViewGroup parent, @LayoutRes int layout) {
        super(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    public abstract void bindHolder(II item);
}