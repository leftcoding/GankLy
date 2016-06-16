package com.gank.gankly.ui.presenter;

import android.app.Activity;

import com.gank.gankly.App;
import com.gank.gankly.config.RefreshStatus;
import com.gank.gankly.config.ViewStatus;
import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.data.entity.UrlCollectDao;
import com.gank.gankly.ui.view.ICollectView;
import com.gank.gankly.utils.ListUtils;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Create by LingYan on 2016-05-12
 */
public class CollectPresenter extends BasePresenter<ICollectView> {
    private UrlCollectDao mUrlCollectDao;
    private QueryBuilder<UrlCollect> queryBuilder;
    private static final int LIMIT = 10;
    private int mPage = 0;
    private ViewStatus mViewStatus;

    public CollectPresenter(Activity mActivity, ICollectView view) {
        super(mActivity, view);
    }

    public void fetchDate(int offset) {
        mUrlCollectDao = App.getDaoSession().getUrlCollectDao();
        queryBuilder = mUrlCollectDao.queryBuilder();
        queryBuilder.orderDesc(UrlCollectDao.Properties.Date);
        queryBuilder.offset(offset).limit(LIMIT);
        callView(queryBuilder.list());
    }

    public void fetchCollect(int page, int refresh) {
        mIView.showRefresh();
        mPage = page;
        int offset = 0;
        if (refresh == RefreshStatus.UP) {
            offset = page * LIMIT;
        }
        fetchDate(offset);
    }

    private void callView(List<UrlCollect> list) {
        mIView.hideRefresh();
        int size = ListUtils.getListSize(list);
        if (size > 0) {
            if (mPage == 0) {
                mIView.refillDate(list);
            } else {
                mIView.appendMoreDate(list);
            }
            mIView.showContent();

            if (size < LIMIT) {
                mIView.hasNoMoreDate();
            }
            mIView.fetchFinish();
        } else {
            if (mPage > 0) {
                mIView.hasNoMoreDate();
            } else {
                if (mViewStatus != ViewStatus.EMPTY) {
                    mIView.showEmpty();
                }
            }
        }
    }

    public void deleteByKey(long id, int size) {
        mUrlCollectDao.deleteByKey(id);
        if (size == 0) {
            mIView.showEmpty();
        }
    }
}
