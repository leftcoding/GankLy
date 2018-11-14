package android.lectcoding.ui.adapter;

import android.support.v7.widget.RecyclerView;

/**
 * Create by LingYan on 2018-03-23
 */

public abstract class  ObserverAdapter<VH extends RecyclerView.ViewHolder> extends BaseAdapter<VH> {
    public ObserverAdapter() {
        if (!isObserverNull()) {
            registerAdapterDataObserver(getObserver());
        }
    }

    @Override
    public void destroy() {
        if (!isObserverNull()) {
            unregisterAdapterDataObserver(getObserver());
        }
    }

    protected abstract RecyclerView.AdapterDataObserver getObserver();

    private boolean isObserverNull() {
        return getObserver() == null;
    }
}
