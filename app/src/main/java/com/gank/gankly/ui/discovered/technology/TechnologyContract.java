package com.gank.gankly.ui.discovered.technology;

import android.content.Context;
import androidx.annotation.NonNull;

import com.gank.gankly.bean.JianDanBean;
import com.gank.gankly.mvp.base.LoadMorePresenter;
import com.gank.gankly.mvp.base.SupportView;

import java.util.List;

/**
 * Create by LingYan on 2016-11-23
 */

public interface TechnologyContract {
    interface View extends SupportView {
        void refillData(List<JianDanBean> list);

        void appendData(List<JianDanBean> list);
    }

    abstract class Presenter extends LoadMorePresenter<View> {

        public Presenter(@NonNull Context context, View view) {
            super(context, view);
        }
    }
}
