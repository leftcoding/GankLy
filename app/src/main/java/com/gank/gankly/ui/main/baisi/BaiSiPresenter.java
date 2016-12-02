package com.gank.gankly.ui.main.baisi;

import com.gank.gankly.bean.BaiSiBean;
import com.gank.gankly.mvp.FetchPresenter;
import com.gank.gankly.mvp.source.remote.BaiSiDataSource;
import com.gank.gankly.utils.ListUtils;
import com.socks.library.KLog;

import java.util.List;

import rx.Subscriber;

/**
 * Create by LingYan on 2016-11-30
 * Email:137387869@qq.com
 */

public class BaiSiPresenter extends FetchPresenter implements BaiSiContract.Presenter {
    private BaiSiDataSource mTask;
    private BaiSiContract.View mView;

    public BaiSiPresenter(BaiSiDataSource task, BaiSiContract.View view) {
        mTask = task;
        mView = view;
    }

    @Override
    public void fetchNew() {
        fetchData(1);
    }

    @Override
    public void fetchMore() {
        fetchData(getFetchPage());
    }

    private void fetchData(int page) {
        mTask.fetchVideo(page).subscribe(new Subscriber<BaiSiBean>() {
            @Override
            public void onCompleted() {
                mView.hideRefresh();
                setFetchPage(getFetchPage() + 1);
            }

            @Override
            public void onError(Throwable e) {
                mView.hideRefresh();
                KLog.e(e);
            }

            @Override
            public void onNext(BaiSiBean baiSiBean) {
                KLog.d("getShowapi_res_body:" + baiSiBean.getShowapi_res_body().getPagebean().getContentlist().size());
                if (baiSiBean.getShowapi_res_body() != null) {
                    List<BaiSiBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean> list = baiSiBean.getShowapi_res_body().getPagebean().getContentlist();
                    if (!ListUtils.isListEmpty(list)) {
                        if (getFetchPage() == 1) {
                            mView.refillData(list);
                        } else {
                            mView.appendData(list);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }
}
