package com.gank.gankly.ui.cure;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gank.gankly.bean.DailyMeiziBean;
import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.mvp.base.LoadMorePresenter;
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

    abstract class Presenter extends LoadMorePresenter<View> {
        public Presenter(@NonNull Context context, View view) {
            super(context, view);
        }

        abstract void girlsImages(String url);
    }
}
