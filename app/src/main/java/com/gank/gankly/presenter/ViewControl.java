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
public class ViewControl {
    private static final int START_PAGE = 1;
    private static final int EMPTY_SIZE = 0;

    public ViewControl() {
    }

    public void onNext(int mPage, int limit, List<?> list, IMeiziView mIView, CallBackViewShow callBackViewShow) {
        int size = ListUtils.getListSize(list);
        boolean hasMore;
        if (size > EMPTY_SIZE) {
            if (mPage == START_PAGE) {
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
            if (mPage == START_PAGE) {
                mIView.showEmpty();
            } else {
                mIView.hasNoMoreDate();
            }
        }
        callBackViewShow.hasMore(hasMore);
    }

    public void onError(int page, boolean isFirst, boolean isNetWork, IMeiziView mIView) {
//        KLog.d("isNetWork:" + isNetWork + ",isFirst:" + isFirst);
        String errString = getErrorTip(isNetWork);
        if (page > START_PAGE) {
            mIView.showRefreshError(errString);
        } else {
            if (isFirst) {
                if (isNetWork) {
                    mIView.showError();
                } else {
                    mIView.showDisNetWork();
                }
            } else {
                mIView.showRefreshError(errString);
            }
        }
    }

    private String getErrorTip(boolean isNetWork) {
        int resId;
        if (isNetWork) {
            resId = R.string.tip_server_error;
        } else {
            resId = R.string.loading_network_failure;
        }
        return App.getAppString(resId);
    }

    public interface CallBackViewShow {
        void hasMore(boolean more);
    }
}
