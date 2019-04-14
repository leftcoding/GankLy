package com.gank.gankly.diff;

import androidx.recyclerview.widget.DiffUtil;

import com.gank.gankly.ui.base.adapter.ItemComparator;

import java.util.List;

/**
 * Create by LingYan on 2018-11-12
 */
public class DiffCallback extends DiffUtil.Callback {
    private List<ItemComparator> oldList;
    private List<ItemComparator> newList;

    public <T extends ItemComparator> DiffCallback(List<T> oldList, List<T> newList) {
        this.oldList = (List<ItemComparator>) oldList;
        this.newList = (List<ItemComparator>) newList;
    }

    @Override
    public int getOldListSize() {
        return oldList == null ? 0 : oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList == null ? 0 : newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        ItemComparator oldItem = oldList.get(oldItemPosition);
        ItemComparator newItem = newList.get(newItemPosition);
        return newItem.isItemSame(oldItem);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        ItemComparator oldItem = oldList.get(oldItemPosition);
        ItemComparator newItem = newList.get(newItemPosition);
        return newItem.isContentEqual(oldItem);
    }
}
