package com.gank.gankly.ui.ios;

import android.content.Context;
import android.ly.business.api.GankServer;
import android.ly.business.domain.Gank;
import android.ly.business.domain.PageConfig;
import android.ly.business.domain.PageEntity;
import androidx.annotation.NonNull;

import com.leftcoding.rxbus.RxApiManager;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Create by LingYan on 2016-12-20
 */

public class IosPresenter extends IosContract.Presenter {
    private PageConfig pageConfig;
    private final AtomicBoolean destroyFlag = new AtomicBoolean(false);

    public IosPresenter(@NonNull Context context, IosContract.View view) {
        super(context, view);
        pageConfig = new PageConfig();
    }

    @Override
    void refreshIos() {
        fetchData();
    }

    @Override
    void appendIos() {
        fetchData();
    }

    private void fetchData() {
        if (destroyFlag.get()) {
            return;
        }
        GankServer.with(context)
                .ios(pageConfig.getCurPage(), pageConfig.limit)
                .doOnSubscribe(disposable -> {
                    if (isViewLife()) {
                        view.showProgress();
                    }
                })
                .doFinally(() -> {
                    if (isViewLife()) {
                        view.hideProgress();
                    }
                })
                .subscribe(new Observer<PageEntity<Gank>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PageEntity<Gank> gankPageEntity) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void destroy() {
        if (destroyFlag.compareAndSet(false, true)) {
            RxApiManager.get().clear(requestTag);
        }
        super.destroy();
    }

    @Override
    protected void onDestroy() {

    }
}
