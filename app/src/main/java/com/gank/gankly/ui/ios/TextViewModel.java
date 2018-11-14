package com.gank.gankly.ui.ios;

import android.ly.business.domain.Gank;

import com.gank.gankly.ui.base.adapter.ItemComparator;

import com.gank.gankly.butterknife.ItemModel;
import com.gank.gankly.utils.DateUtils;

import java.util.Date;

/**
 * Create by LingYan on 2018-11-12
 */
public class TextViewModel extends ItemModel {
    Gank gank;

    TextViewModel(Gank gank) {
        this.gank = gank;
    }

    public String getTime() {
        Date date = DateUtils.formatToDate(gank.publishedAt);
        return DateUtils.formatString(date, DateUtils.MM_DD);
    }

    @Override
    public boolean isItemSame(ItemComparator itemComparator) {
        return itemComparator instanceof TextViewModel;
    }

    @Override
    public boolean isContentEqual(ItemComparator itemComparator) {
        if (itemComparator instanceof TextViewModel) {
            TextViewModel oldItem = (TextViewModel) itemComparator;
            return gank.isContentEqual(oldItem.gank);
        }
        return false;
    }

    @Override
    public int getViewType() {
        return ViewType.NORMAL;
    }
}
