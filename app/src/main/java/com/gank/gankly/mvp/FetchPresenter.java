package com.gank.gankly.mvp;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.bean.GankResult;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.utils.ListUtils;

import java.util.List;

/**
 * Create by LingYan on 2016-10-25
 * Email:137387869@qq.com
 */

public abstract class FetchPresenter extends BasePresenter {
    private int fetchLimit = 20;
    private int fetchPage = 1;
    private boolean hasMore = true;

    public int getInitPage() {
        return fetchPage = 1;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public int getFetchLimit() {
        return fetchLimit;
    }

    public void setFetchLimit(int fetchLimit) {
        this.fetchLimit = fetchLimit;
    }

    public int getFetchPage() {
        return fetchPage;
    }

    public void setFetchPage(int fetchPage) {
        this.fetchPage = fetchPage;
    }

    public List<ResultsBean> filterData(GankResult gankResult, IFetchView view) {
        view.hideRefresh();
        if (gankResult != null) {
            int size = gankResult.getSize();
            if (size > 0) {
                if (size >= fetchLimit) {
                    view.showContent();
                } else {
                    view.hasNoMoreDate();
                }
                return gankResult.getResults();
            } else {
                if (fetchPage == 1) {
                    view.showEmpty();
                } else {
                    view.hasNoMoreDate();
                }
            }
        } else {
            if (fetchPage > 1) {
                view.showRefreshError(App.getAppString(R.string.loading_error));
            } else {
                view.showError();
            }
        }
        return null;
    }

    public <T> List<T> filterData(List<T> t, IFetchView view) {
        view.hideRefresh();
        if (t != null) {
            int size = ListUtils.getListSize(t);
            if (size > 0) {
                if (size >= fetchLimit) {
                    view.showContent();
                } else {
                    view.hasNoMoreDate();
                }
                return t;
            } else {
                if (fetchPage == 1) {
                    view.showEmpty();
                } else {
                    view.hasNoMoreDate();
                }
            }
        } else {
            if (fetchPage > 1) {
                view.showRefreshError(App.getAppString(R.string.loading_error));
            } else {
                view.showError();
            }
        }
        return null;
    }

    public void parseError(IFetchView view) {
        view.hideRefresh();
        if (App.isNetConnect()) {
            if (fetchPage > 1) {
                view.showRefreshError(App.getAppString(R.string.loading_error));
            } else {
                view.showError();
            }
        } else {
            if (fetchPage > 1) {
                view.showRefreshError(App.getAppString(R.string.loading_network_failure));
            } else {
                view.showDisNetWork();
            }
        }
    }
}
