package com.gank.gankly.ui.web.normal;

import android.support.annotation.NonNull;

import com.gank.gankly.data.entity.ReadHistory;
import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.mvp.ISubscribePresenter;
import com.gank.gankly.mvp.base.BaseView;

/**
 * Create by LingYan on 2016-10-27
 */

public interface WebContract {
    interface View extends BaseView {
        void onCollect();

        void onCancelCollect();

        UrlCollect getCollect();

        void setCollectIcon(boolean isCollect);
    }

    interface Presenter extends ISubscribePresenter {
        void findCollectUrl(@NonNull String url);

        void insetHistoryUrl(@NonNull ReadHistory readHistory);

        void cancelCollect();

        void collectAction(boolean isCollect);

        void collect();
    }
}
