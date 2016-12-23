package com.gank.gankly.ui.collect;

import android.support.annotation.NonNull;

import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.mvp.BasePresenter;
import com.gank.gankly.mvp.source.LocalDataSource;
import com.gank.gankly.utils.ListUtils;
import com.socks.library.KLog;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Create by LingYan on 2016-05-12
 */
public class CollectPresenter extends BasePresenter implements CollectContract.Presenter {
    private static final int LIMIT = 10;

    @NonNull
    private LocalDataSource mTask;
    @NonNull
    private CollectContract.View mModelView;

    private int mPage = 0;
    private boolean isNoMore;
    private Disposable disposable;

    public CollectPresenter(@NonNull LocalDataSource task, @NonNull CollectContract.View modelView) {
        this.mTask = task;
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
        mTask.getCollect(offset, LIMIT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<UrlCollect>>() {
                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                    }

                    @Override
                    public void onComplete() {
                        mModelView.hideRefresh();
                        mPage = mPage + 1;
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<UrlCollect> urlCollects) {
                        parseData(urlCollects);
                    }
                });
    }

    @Override
    public void cancelCollect(final long position) {
        disposable = mTask.cancelCollect(position)
                .delaySubscription(4, TimeUnit.SECONDS)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        KLog.e(throwable);
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        if (mModelView.getItemsCount() == 0) {
                            mModelView.showEmpty();
                        }
                    }
                });
    }

    @Override
    public void backCollect() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }
}
