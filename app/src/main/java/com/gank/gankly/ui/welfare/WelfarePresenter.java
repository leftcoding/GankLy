package com.gank.gankly.ui.welfare;

import android.content.Context;
import android.lectcoding.ui.logcat.Logcat;
import android.ly.business.api.GankServer;
import android.ly.business.domain.Gank;
import android.ly.business.domain.PageEntity;

import com.leftcoding.network.base.Server;

/**
 * Create by LingYan on 2016-12-23
 */

public class WelfarePresenter extends WelfareContract.Presenter {
    private static final int DEFAULT_LIMIT = 20;

    WelfarePresenter(Context context, WelfareContract.View view) {
        super(context, view);
    }

    @Override
    public void loadWelfare(final int page) {
        showProgress();
        GankServer.with(context)
                .images(requestTag, page, DEFAULT_LIMIT, new Server.ConsumerCall<PageEntity<Gank>>() {
                    @Override
                    public void onNext(PageEntity<Gank> pageEntity) throws Exception {
                        hideProgress();
                        if (pageEntity != null) {
                            view.loadWelfareSuccess(page, pageEntity.results);
                            return;
                        }

                        if (view != null) {
                            view.loadWelfareFailure("获取数据失败");
                        }
                    }

                    @Override
                    public void accept(Throwable throwable) {
                        hideProgress();
                        Logcat.e(throwable);
                        if (view != null) {
                            view.loadWelfareFailure("获取数据失败");
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {

    }
}
