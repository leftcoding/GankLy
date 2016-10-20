package com.gank.gankly.ui.collect;

import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.presenter.IBaseAsynDataSource;
import com.gank.gankly.view.FetchView;

import java.util.List;

/**
 * Create by LingYan on 2016-10-20
 * Email:137387869@qq.com
 */

public interface CollectContract {

    interface View extends FetchView<Presenter> {
        void setAdapterList(List<UrlCollect> list);

        void appendAdapter(List<UrlCollect> list);

        void hasNoMoreDate();
    }

    interface Presenter extends IBaseAsynDataSource {
    }
}
