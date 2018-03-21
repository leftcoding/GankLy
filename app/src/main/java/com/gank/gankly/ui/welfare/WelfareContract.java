package com.gank.gankly.ui.welfare;

import android.ly.business.domain.Gank;

import com.gank.gankly.mvp.ILoadMorePresenter;
import com.gank.gankly.mvp.base.SupportView;

import java.util.List;

/**
 * Create by LingYan on 2016-12-23
 * Email:137387869@qq.com
 */

public class WelfareContract {
    interface View extends SupportView {
        void refreshData(List<Gank> list);

        void appendData(List<Gank> list);

        void refershDataFailure(String msg);

        void appendWelfareFailure(String msg);
    }

    interface Presenter extends ILoadMorePresenter {

    }
}
