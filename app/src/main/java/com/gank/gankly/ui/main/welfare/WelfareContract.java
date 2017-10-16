package com.gank.gankly.ui.main.welfare;

import com.gank.gankly.mvp.ILoadMorePresenter;
import com.gank.gankly.mvp.base.SupportView;
import com.leftcoding.http.bean.ResultsBean;

import java.util.List;

/**
 * Create by LingYan on 2016-12-23
 * Email:137387869@qq.com
 */

public class WelfareContract {
    interface View extends SupportView {
        void refillData(List<ResultsBean> list);

        void appendData(List<ResultsBean> list);
    }

    interface Presenter extends ILoadMorePresenter {
    }
}
