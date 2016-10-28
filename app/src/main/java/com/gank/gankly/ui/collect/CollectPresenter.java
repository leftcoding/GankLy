package com.gank.gankly.ui.collect;

import android.support.annotation.NonNull;

import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.mvp.BasePresenter;
import com.gank.gankly.mvp.source.LocalDataSource;
import com.gank.gankly.utils.ListUtils;
import com.socks.library.KLog;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Create by LingYan on 2016-05-12
 */
public class CollectPresenter extends BasePresenter implements CollectContract.Presenter {

    private static final int LIMIT = 10;

    @NonNull
    private LocalDataSource mModel;
    @NonNull
    private CollectContract.View mModelView;

    private int mPage = 0;
    private boolean isNoMore;

    public CollectPresenter(@NonNull LocalDataSource model, @NonNull CollectContract.View modelView) {
        this.mModel = model;
        this.mModelView = modelView;
    }

    private void parseData(List<UrlCollect> list) {
        int size = ListUtils.getListSize(list);
        if (size > 0) {
            if (mPage == 0) {
                mModelView.setAdapterList(list);
            } else {
                mModelView.appendAdapter(list);
            }
            mModelView.showContent();

            if (size < LIMIT) {
                isNoMore = true;
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

    @Override
    public void fetchNew() {
        isNoMore = false;
        mPage = 0;
        int offset = getOffset();
        collect(offset);
    }

    @Override
    public void fetchMore() {
        if (!isNoMore) {
            mModelView.showRefresh();
            int offset = getOffset();
            collect(offset);
        }
    }

    private int getOffset() {
        return mPage * LIMIT;
    }

    private void collect(int offset) {
        mModel.getCollect(offset, LIMIT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<UrlCollect>>() {
                    @Override
                    public void onCompleted() {
                        mModelView.hideRefresh();
                        mPage = mPage + 1;
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                    }

                    @Override
                    public void onNext(List<UrlCollect> urlCollects) {
                        parseData(urlCollects);
                    }
                });
    }

    @Override
    public void cancelCollect(long position) {
        mModel.cancelCollect(position)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        if (mModelView.getItemsCount() == 0) {
                            mModelView.showEmpty();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String o) {
                        mModelView.onDelete();
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
