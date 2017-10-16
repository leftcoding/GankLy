package com.gank.gankly.ui.discovered.video;

import com.gank.gankly.mvp.ILoadMorePresenter;
import com.gank.gankly.mvp.base.SupportView;
import com.leftcoding.http.bean.ResultsBean;

import java.util.List;

/**
 * Create by LingYan on 2017-01-03
 * Email:137387869@qq.com
 */

public interface VideoContract {
    interface View extends SupportView {
        void refillData(List<ResultsBean> list);

        void appendData(List<ResultsBean> list);
    }

    interface Presenter extends ILoadMorePresenter {

    }
}

