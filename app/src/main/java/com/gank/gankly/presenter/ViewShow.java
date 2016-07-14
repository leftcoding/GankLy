package com.gank.gankly.presenter;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.utils.ListUtils;
import com.gank.gankly.view.IMeiziView;
import com.socks.library.KLog;

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

    public void callError(int page, boolean isFirst, boolean isNetWork, IMeiziView mIView) {
        KLog.d("isNetWork:" + isNetWork + ",isFirst:" + isFirst);
        String errString = getError(isNetWork);
        KLog.d("errString:" + errString);
        if (page > 1) {
            mIView.showRefreshError(errString);
        } else {
            if (isFirst) {
                if (isNetWork) {
                    mIView.showError();
                } else {
                    mIView.showDisNetWork();
                }
            } else {
                KLog.d("------");
                mIView.showRefreshError(errString);
            }
        }
    }

    private String getError(boolean isNetWork) {
        KLog.d("isNetWork:" + isNetWork);
        int resId;
        if (isNetWork) {
            resId = R.string.tip_server_error;
        } else {
            resId = R.string.loading_network_failure;
        }
        KLog.d("App.getAppString(resId):" + App.getAppString(resId));
        return App.getAppString(resId);
    }

    public interface CallBackViewShow {
        void hasMore(boolean more);
    }
}
