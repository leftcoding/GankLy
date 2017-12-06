package com.gank.gankly.ui.discovered.teamBlog;

import com.gank.gankly.bean.JianDanBean;
import com.gank.gankly.mvp.ILoadMorePresenter;
import com.gank.gankly.mvp.base.SupportView;

import java.util.List;

/**
 * Create by LingYan on 2016-11-23
 */

public interface TeamBlogContract {
    interface View extends SupportView {
        void refillData(List<JianDanBean> list);

        void appendData(List<JianDanBean> list);
    }

    interface Presenter extends ILoadMorePresenter {

    }
}
