package com.gank.gankly.ui.daily;

import android.content.Context;
import android.ly.business.domain.Gift;
import androidx.annotation.NonNull;

import android.ly.business.domain.Girl;
import com.gank.gankly.mvp.base.LoadMorePresenter;
import com.gank.gankly.mvp.base.SupportView;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by LingYan on 2016-10-26
 */

public interface DailyGirlContract {
    interface View extends SupportView {
        void refillData(List<Girl> list);

        void appendItem(List<Girl> list);

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
