package com.gank.gankly.ui.presenter;

import android.app.Activity;

import com.gank.gankly.App;
import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.data.entity.UrlCollectDao;
import com.gank.gankly.ui.view.IBaseView;
import com.gank.gankly.ui.view.ICollectView;
import com.gank.gankly.utils.ListUtils;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Create by LingYan on 2016-05-12
 */
public class CollectPresenter extends BasePresenter<ICollectView> {
    private UrlCollectDao mUrlCollectDao;
    QueryBuilder<UrlCollect> queryBuilder;
    private int mLimit = 10;
    private int mPage = 0;
    private boolean isMore = true;

    public CollectPresenter(Activity mActivity, ICollectView view) {
        super(mActivity, view);
    }

    public void fetchDate(int page) {
        if (page == 0) {
            isMore = true;
        }
        mPage = page;
        int offSet = page * mLimit;
        mUrlCollectDao = App.getDaoSession().getUrlCollectDao();
        queryBuilder = mUrlCollectDao.queryBuilder();
        queryBuilder.orderDesc(UrlCollectDao.Properties.Date);
        queryBuilder.offset(offSet).limit(mLimit);
        toView(queryBuilder.list());
    }

    private void toView(List<UrlCollect> list) {
        int size = ListUtils.getListSize(list);
        if (size > 0) {
            if (mPage == 0) {
                if (mIView.getCurStatus() != IBaseView.ViewStatus.SHOW) {
                    mIView.showView();
                }
                mIView.refillDate(list);
            } else {
                mIView.appendMoreDate(list);
            }

            if (size < mLimit) {
                isMore = false;
                mIView.hasNoMoreDate();
            }
            mIView.fetchFinish();
        } else {
            if (mPage > 0) {
                mIView.hasNoMoreDate();
            } else {
                if (mIView.getCurStatus() != IBaseView.ViewStatus.EMPTY) {
                    mIView.showEmpty();
                }
            }
            isMore = false;
        }
        mIView.hideRefresh();
    }

    public void deleteByKey(long id) {
        mUrlCollectDao.deleteByKey(id);
        if (mIView.isListEmpty()) {
            mIView.showEmpty();
        }
    }

    public boolean isLoadMore() {
        return isMore;
    }
}
