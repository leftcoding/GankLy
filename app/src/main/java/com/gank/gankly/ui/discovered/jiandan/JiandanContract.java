package com.gank.gankly.ui.discovered.jiandan;

import com.gank.gankly.bean.JianDanBean;
import com.gank.gankly.mvp.ILoadMorePresenter;
import com.gank.gankly.mvp.base.SupportView;

import java.util.List;

/**
 * Create by LingYan on 2016-11-21
 */

public interface JiandanContract {
    interface View extends SupportView {
        void refillData(List<JianDanBean> list);

        void appendMoreDate(List<JianDanBean> list);
    }

    interface Presenter extends ILoadMorePresenter {

    }
}
