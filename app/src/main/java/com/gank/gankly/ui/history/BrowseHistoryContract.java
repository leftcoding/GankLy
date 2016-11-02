package com.gank.gankly.ui.history;

import com.gank.gankly.data.entity.ReadHistory;
import com.gank.gankly.mvp.IFetchPresenter;
import com.gank.gankly.mvp.IFetchView;

import java.util.List;

/**
 * Create by LingYan on 2016-10-31
 * Email:137387869@qq.com
 */

public interface BrowseHistoryContract {
    interface View extends IFetchView {
        void refillData(List<ReadHistory> history);
    }

    interface Presenter extends IFetchPresenter {

    }
}
