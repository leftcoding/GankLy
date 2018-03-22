package com.gank.gankly.ui.collect;

import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.mvp.ILoadMorePresenter;
import com.gank.gankly.mvp.base.SupportView;

import java.util.List;

/**
 * Create by LingYan on 2016-10-20
 */

public interface CollectContract {

    interface View extends SupportView {
        void setAdapterList(List<UrlCollect> list);

        void appendAdapter(List<UrlCollect> list);

        void onDelete();

        int getItemsCount();

        void revokeCollect();
    }

    abstract class Presenter implements ILoadMorePresenter {
        abstract void cancelCollect(long position);

        abstract void insertCollect(UrlCollect collect);
    }
}
