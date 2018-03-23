package android.lectcoding.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Create by LingYan on 2017-11-27
 */

public abstract class BasicHolder<II extends ViewItem> extends RecyclerView.ViewHolder {
    public BasicHolder(ViewGroup parent, @LayoutRes int layout) {
        super(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    public abstract void bindHolder(II item);
}
