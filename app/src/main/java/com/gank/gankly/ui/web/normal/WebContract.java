package com.gank.gankly.ui.web.normal;

import android.support.annotation.NonNull;

import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.mvp.IBasePresenter;
import com.gank.gankly.mvp.IBaseView;

import java.util.List;

/**
 * Create by LingYan on 2016-10-27
 * Email:137387869@qq.com
 */

public interface WebContract {
    interface View extends IBaseView {
        void findCollectSuccess(List<UrlCollect> list);

        void findHistoryUrlSuccess();
    }

    interface Presenter extends IBasePresenter {
        void findCollectUrl(@NonNull String url);

        void findHistoryUrl(@NonNull String url);
    }
}
