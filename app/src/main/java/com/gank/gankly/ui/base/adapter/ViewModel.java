package com.gank.gankly.ui.base.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Create by LingYan on 2018-09-26
 */
public abstract class ViewModel<VH extends RecyclerView.ViewHolder> implements ItemComparator {
    public abstract int getViewType();

    public abstract int getSpanSize(int size, int position);

    public abstract VH createViewHolder(ViewGroup parent);

    public abstract void bindView(VH holder);

    public abstract void bindView(VH holder, int position, List payloads);

    public abstract int getLayoutRes();
}
