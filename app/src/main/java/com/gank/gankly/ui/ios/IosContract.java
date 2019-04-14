package com.gank.gankly.ui.ios;

import android.content.Context;
import android.ly.business.domain.Gank;
import androidx.annotation.NonNull;

import com.gank.gankly.mvp.base.LoadMorePresenter;
import com.gank.gankly.mvp.base.PageView;

/**
 * Create by LingYan on 2016-12-20
 */

public interface IosContract {
    interface View extends PageView<Gank> {
    }

    abstract class Presenter extends LoadMorePresenter<View> {

        public Presenter(@NonNull Context context, View view) {
            super(context, view);
        }

        abstract void refreshIos();

        abstract void appendIos();
    }
}
