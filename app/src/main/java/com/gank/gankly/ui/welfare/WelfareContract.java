package com.gank.gankly.ui.welfare;

import android.content.Context;
import android.ly.business.domain.Gank;
import android.support.annotation.NonNull;

import com.gank.gankly.mvp.base.LoadMorePresenter;
import com.gank.gankly.mvp.base.SupportView;

import java.util.List;

/**
 * Create by LingYan on 2016-12-23
 */

public interface WelfareContract {
    interface View extends SupportView {
        void loadWelfareSuccess(int page, List<Gank> list);

        void loadWelfareFailure(String msg);

        void loadDataFailure(String msg);

        void appendWelfareFailure(String msg);
    }

    public abstract class Presenter extends LoadMorePresenter<View> {

        public Presenter(@NonNull Context context, View view) {
            super(context, view);
        }

        public abstract void loadWelfare(int page);
    }
}
