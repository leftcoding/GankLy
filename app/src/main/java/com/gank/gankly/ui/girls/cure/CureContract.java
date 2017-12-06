package com.gank.gankly.ui.girls.cure;

import com.gank.gankly.bean.DailyMeiziBean;
import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.mvp.ILoadMorePresenter;
import com.gank.gankly.mvp.base.SupportView;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by LingYan on 2016-10-26
 */

public interface CureContract {
    interface View extends SupportView {
        void refillData(List<DailyMeiziBean> list);

        void appendItem(List<DailyMeiziBean> list);

        void setMaxProgress(int value);

        void disProgressDialog();

        void openBrowseActivity(ArrayList<GiftBean> list);
    }

    interface Presenter extends ILoadMorePresenter {
        void girlsImages(String url);
    }
}
