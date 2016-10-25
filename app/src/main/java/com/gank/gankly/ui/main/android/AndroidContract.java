package com.gank.gankly.ui.main.android;

import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.mvp.IFetchPresenter;
import com.gank.gankly.mvp.IFetchView;

import java.util.List;

/**
 * Create by LingYan on 2016-10-25
 * Email:137387869@qq.com
 */

public interface AndroidContract {
    interface View extends IFetchView {
        void refillDate(List<ResultsBean> list);

        void appendData(List<ResultsBean> list);
    }

    interface Presenter extends IFetchPresenter {

    }
}
