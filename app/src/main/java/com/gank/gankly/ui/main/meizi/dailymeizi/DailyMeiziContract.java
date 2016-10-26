package com.gank.gankly.ui.main.meizi.dailymeizi;

import com.gank.gankly.bean.DailyMeiziBean;
import com.gank.gankly.mvp.IFetchPresenter;
import com.gank.gankly.mvp.IFetchView;

import java.util.List;

/**
 * Create by LingYan on 2016-10-26
 * Email:137387869@qq.com
 */

public interface DailyMeiziContract {
    interface View extends IFetchView {
        void refillData(List<DailyMeiziBean> list);

        void setMaxProgress(int value);

        void disProgressDialog();

        void setProgressValue(int value);
    }

    interface Presenter extends IFetchPresenter {
        void girlsImages(String url);
    }
}
