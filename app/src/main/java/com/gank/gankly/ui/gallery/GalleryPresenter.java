package com.gank.gankly.ui.gallery;

import com.gank.gankly.bean.GankResult;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.mvp.FetchPresenter;
import com.gank.gankly.mvp.source.remote.GankDataSource;
import com.gank.gankly.utils.CrashUtils;
import com.gank.gankly.utils.ListUtils;
import com.leftcoding.http.bean.ResultsBean;
import com.socks.library.KLog;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2017-01-16
 * Email:137387869@qq.com
 */

public class GalleryPresenter extends FetchPresenter implements GalleryContract.Presenter {
    private GankDataSource mTask;
    private GalleryContract.View mModelView;
    private boolean isFetch;

    public GalleryPresenter(GankDataSource task, GalleryContract.View view) {
        mTask = task;
        mModelView = view;
    }

    @Override
    public void fetchNew() {

    }

    @Override
    public void fetchMore() {
        if (!isFetch) {
            int nextPage = MeiziArrayList.getInstance().getPage() + 1;
            mTask.fetchWelfare(nextPage, getFetchLimit())
                    .subscribe(new Observer<GankResult>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            isFetch = true;
                        }

                        @Override
                        public void onNext(GankResult gankResult) {
                            List<ResultsBean> list = filterData(gankResult.getResults(), mModelView);
                            if (ListUtils.getSize(list) > 0) {
                                mModelView.appendData(list);
                                mModelView.sysNumText();
                                MeiziArrayList.getInstance().addImages(gankResult.getResults(), nextPage);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            KLog.e(e);
                            CrashUtils.crashReport(e);
                            parseError(mModelView);
                        }

                        @Override
                        public void onComplete() {
                            isFetch = false;
                        }
                    });
        }
    }

    @Override
    public void unSubscribe() {

    }
}
