package com.gank.gankly.mvp;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.bean.GankResult;
import com.gank.gankly.mvp.base.SupportView;
import com.gank.gankly.utils.ListUtils;
import com.leftcoding.http.bean.ResultsBean;

import java.util.List;

/**
 * Create by LingYan on 2016-10-25
 */

public abstract class FetchPresenter implements ISubscribePresenter {
    private int fetchLimit = 20;
    private int fetchPage = 1;
    private boolean hasMore = true;
    private int offSetPage = 0;

    public int getOffSet() {
        return offSetPage * fetchLimit;
    }

    public void setOffSetPage(int page) {
        this.offSetPage = page;
    }

    public int getOffSetPage() {
        return offSetPage;
    }

    public int getInitPage() {
        return fetchPage = 1;
    }

    public boolean hasMore() {
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

    public void setFetchPage(int fetchPage) {
        this.fetchPage = fetchPage;
    }

    public int getFetchPage() {
        return fetchPage;
    }

    public List<ResultsBean> filterData(GankResult gankResult, SupportView view) {
        view.hideProgress();
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

    public <T> List<T> filterData(List<T> t, SupportView view) {
        view.hideProgress();
        if (t != null) {
            int size = ListUtils.getSize(t);
            if (size > 0) {
                if (size >= fetchLimit) {
                    view.showContent();
                } else {
                    hasMore = false;
                    view.hasNoMoreDate();
                }
                return t;
            } else {
                if (fetchPage == 1) {
                    view.showEmpty();
                } else {
                    hasMore = false;
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

    /**
     * 过滤数据，数据加载完、空数据、显示数据
     *
     * @param t    集合
     * @param view IFetch 视图
     * @param <T>  实体类
     * @return 最后返回数据集合
     */
    public <T> List<T> filterDataBase(List<T> t, SupportView view) {
        if (t != null) {
            int size = ListUtils.getSize(t);
            if (size > 0) {
                if (size >= fetchLimit) {
                    view.showContent();
                } else {
                    hasMore = false;
                    view.hasNoMoreDate();
                }
                return t;
            } else {
                if (offSetPage == 0) {
                    view.showEmpty();
                } else {
                    hasMore = false;
                    view.hasNoMoreDate();
                }
            }
        } else {
            if (offSetPage > 0) {
                view.showRefreshError(App.getAppString(R.string.loading_error));
            } else {
                view.showError();
            }
        }
        return null;
    }

    /**
     * 解析请求失败，显示网络错误、服务器问题
     *
     * @param view IFetch 视图
     */
    public void parseError(SupportView view) {
        view.hideProgress();
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
