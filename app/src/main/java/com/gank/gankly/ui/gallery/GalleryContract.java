package com.gank.gankly.ui.gallery;

import android.ly.business.domain.Gank;

import com.gank.gankly.mvp.ILoadMorePresenter;
import com.gank.gankly.mvp.base.SupportView;

import java.util.List;

/**
 * Create by LingYan on 2017-01-16
 * Email:137387869@qq.com
 */

public interface GalleryContract {

    interface View extends SupportView {
        void appendData(List<Gank> list);

        void sysNumText();
    }

    interface Presenter extends ILoadMorePresenter {

    }
}
