package android.lectcoding.ui.adapter;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Create by LingYan on 2017-10-14
 */

public abstract class BaseAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    public abstract void destroy();
}
