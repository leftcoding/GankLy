package com.gank.gankly.ui.history;

import com.gank.gankly.data.entity.ReadHistory;
import com.gank.gankly.mvp.ILoadMorePresenter;
import com.gank.gankly.mvp.base.SupportView;

import java.util.List;

/**
 * Create by LingYan on 2016-10-31
 * Email:137387869@qq.com
 */

public interface BrowseHistoryContract {
    interface View extends SupportView {
        void refillData(List<ReadHistory> history);

        void appendData(List<ReadHistory> history);
    }

    interface Presenter extends ILoadMorePresenter {
        void deleteHistory(long id);
    }
}
