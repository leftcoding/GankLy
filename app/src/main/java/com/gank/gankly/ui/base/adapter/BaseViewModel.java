package com.gank.gankly.ui.base.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Create by LingYan on 2018-09-26
 */
public abstract class BaseViewModel<VH extends RecyclerView.ViewHolder> extends ViewModel<VH> {
    protected Context context;

    @Override
    public void bindView(VH holder, int position, List payloads) {
    }

    @Override
    public void bindView(VH holder) {
        context = holder.itemView.getContext();
    }

    @Override
    public int getSpanSize(int size, int position) {
        return size;
    }

    @Override
    public boolean isItemSame(ItemComparator itemComparator) {
        return false;
    }

    @Override
    public boolean isContentEqual(ItemComparator itemComparator) {
        return false;
    }

    @Override
    public int getViewType() {
        return getLayoutRes();
    }

    protected View createView(ViewGroup viewGroup) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(getLayoutRes(), viewGroup, false);
    }
}
