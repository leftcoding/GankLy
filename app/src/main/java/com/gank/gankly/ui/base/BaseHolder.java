package com.gank.gankly.ui.base;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Create by LingYan on 2016-11-10
 */

public abstract class BaseHolder extends RecyclerView.ViewHolder {
    public boolean isShowing;

    public BaseHolder(View itemView) {
        super(itemView);
    }

    public abstract View getView();
}
