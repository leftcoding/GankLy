package com.gank.gankly.ui.main.welfare;

import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.mvp.IFetchPresenter;
import com.gank.gankly.mvp.IFetchView;

import java.util.List;

/**
 * Create by LingYan on 2016-12-23
 * Email:137387869@qq.com
 */

public class WelfareContract {
    interface View extends IFetchView {
        void refillData(List<ResultsBean> list);

        void appendData(List<ResultsBean> list);
    }

    interface Presenter extends IFetchPresenter {
    }
}
