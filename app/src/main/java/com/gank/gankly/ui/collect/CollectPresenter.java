package com.gank.gankly.ui.collect;

import com.gank.gankly.App;
import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.data.entity.UrlCollectDao;
import com.gank.gankly.utils.ListUtils;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Create by LingYan on 2016-05-12
 */
public class CollectPresenter extends CollectContract.Presenter {
    private static final int LIMIT = 10;

    private UrlCollectDao mUrlCollectDao;
    private int mPage = 0;

    public CollectPresenter() {
    }

    private void fetchDate(int offset) {
        mUrlCollectDao = App.getDaoSession().getUrlCollectDao();
        QueryBuilder<UrlCollect> queryBuilder = mUrlCollectDao.queryBuilder();
        queryBuilder.orderDesc(UrlCollectDao.Properties.Date);
        queryBuilder.offset(offset).limit(LIMIT);
        parseData(queryBuilder.list());
    }

    private void parseData(List<UrlCollect> list) {
        mModelView.hideRefresh();
        int size = ListUtils.getListSize(list);
        if (size > 0) {
            if (mPage == 0) {
                mModelView.setAdapterList(list);
            } else {
                mModelView.appendAdapter(list);
            }
            mModelView.showContent();

            if (size < LIMIT) {
                mModelView.hasNoMoreDate();
            }
        } else {
            if (mPage > 0) {
                mModelView.hasNoMoreDate();
            } else {
                mModelView.showEmpty();
            }
        }
    }

    public void deleteByKey(long id, int size) {
        mUrlCollectDao.deleteByKey(id);
        if (size == 0) {
            mModelView.showEmpty();
        }
    }

    @Override
    public void fetchNew() {
        int offset = 0;
        fetchDate(offset);
    }

    @Override
    public void fetchMore() {
        int offset = mPage * LIMIT;
        fetchDate(offset);
    }
}
