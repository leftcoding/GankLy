package com.gank.gankly.ui.dailymeizi;

import android.content.Context;
import android.ly.business.domain.Gift;
import android.support.annotation.NonNull;

import android.ly.business.domain.DailyMeizi;
import com.gank.gankly.mvp.base.LoadMorePresenter;
import com.gank.gankly.mvp.base.SupportView;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by LingYan on 2016-10-26
 */

public interface DailyMeiziContract {
    interface View extends SupportView {
        void refillData(List<DailyMeizi> list);

        void appendItem(List<DailyMeizi> list);

        void setMaxProgress(int value);

        void disProgressDialog();

        void openBrowseActivity(ArrayList<Gift> list);
    }

    abstract class Presenter extends LoadMorePresenter<View> {
        public Presenter(@NonNull Context context, View view) {
            super(context, view);
        }

        abstract void girlsImages(String url);
    }
}
