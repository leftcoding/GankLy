package com.gank.gankly.butterknife;

import com.gank.gankly.ui.base.adapter.ItemComparator;

/**
 * Create by LingYan on 2018-11-12
 */
public abstract class ItemModel implements ItemComparator {
    public abstract int getViewType();

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
}
