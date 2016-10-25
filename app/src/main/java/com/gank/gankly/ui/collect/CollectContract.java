package com.gank.gankly.ui.collect;

import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.mvp.IFetchPresenter;
import com.gank.gankly.mvp.IFetchView;

import java.util.List;

/**
 * Create by LingYan on 2016-10-20
 * Email:137387869@qq.com
 */

public interface CollectContract {

    interface View extends IFetchView {
        void setAdapterList(List<UrlCollect> list);

        void appendAdapter(List<UrlCollect> list);

        void onDelete();

        int getItemsCount();
    }

    interface Presenter extends IFetchPresenter {
        void delete(long position);
    }
}
