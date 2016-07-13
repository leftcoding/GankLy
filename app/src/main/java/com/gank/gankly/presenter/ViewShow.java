package com.gank.gankly.presenter;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.utils.ListUtils;
import com.gank.gankly.view.IMeiziView;

import java.util.List;

/**
 * Create by LingYan on 2016-07-13
 * Email:137387869@qq.com
 */
public class ViewShow {
    public ViewShow() {
    }

    public void callShow(int mPage, int limit, List<?> list, IMeiziView mIView, CallBackViewShow callBackViewShow) {
        int size = ListUtils.getListSize(list);
        boolean hasMore;
        if (size > 0) {
            if (mPage == 1) {
                mIView.clear();
                mIView.refillDate(list);
            } else {
                mIView.appendMoreDate(list);
            }
            if (size < limit) {
                hasMore = false;
                mIView.hasNoMoreDate();
            } else {
                hasMore = true;
            }
        } else {
            hasMore = false;
            if (mPage == 1) {
                mIView.showEmpty();
            } else {
                mIView.hasNoMoreDate();
            }
        }
        callBackViewShow.hasMore(hasMore);
    }

    public void callError(int page, boolean isNetWork, IMeiziView mIView) {
        if (page > 1) {
            int resId = R.string.loading_network_failure;
            if (isNetWork) {
                resId = R.string.tip_server_error;
            }
            mIView.showRefreshError(App.getAppString(resId));
        } else {
            if (isNetWork) {
                mIView.showError();
            } else {
                mIView.showDisNetWork();
            }
        }
    }

    public interface CallBackViewShow {
        void hasMore(boolean hasMore);
    }
}
