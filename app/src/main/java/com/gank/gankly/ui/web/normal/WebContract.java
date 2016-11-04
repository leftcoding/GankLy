package com.gank.gankly.ui.web.normal;

import android.support.annotation.NonNull;

import com.gank.gankly.data.entity.ReadHistory;
import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.mvp.IBasePresenter;
import com.gank.gankly.mvp.IBaseView;

/**
 * Create by LingYan on 2016-10-27
 * Email:137387869@qq.com
 */

public interface WebContract {
    interface View extends IBaseView {
        void onCollect();

        void onCancelCollect();

        UrlCollect getCollect();

        void setCollectIcon(boolean isCollect);
    }

    interface Presenter extends IBasePresenter {
        void findCollectUrl(@NonNull String url);

        void insetHistoryUrl(@NonNull ReadHistory readHistory);

        void cancelCollect();

        void collectAction(boolean isCollect);

        void collect();
    }
}
