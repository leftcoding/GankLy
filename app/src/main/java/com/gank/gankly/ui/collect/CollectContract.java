package com.gank.gankly.ui.collect;

import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.mvp.BaseModel;
import com.gank.gankly.mvp.FetchPresenter;
import com.gank.gankly.mvp.FetchView;

import java.util.List;

/**
 * Create by LingYan on 2016-10-20
 * Email:137387869@qq.com
 */

public interface CollectContract {

    interface View extends FetchView {
        void setAdapterList(List<UrlCollect> list);

        void appendAdapter(List<UrlCollect> list);
    }

    interface Model extends BaseModel {

    }

    abstract class Presenter extends FetchPresenter<Model, View> {

    }
}
