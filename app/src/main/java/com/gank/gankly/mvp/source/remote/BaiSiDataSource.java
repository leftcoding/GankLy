package com.gank.gankly.mvp.source.remote;

import android.support.annotation.Nullable;

import com.gank.gankly.bean.BaiSiBean;
import com.gank.gankly.bean.BuDeJieBean;
import com.gank.gankly.bean.BuDeJieVideo;
import com.gank.gankly.mvp.source.BaseDataSourceModel;
import com.gank.gankly.network.api.BaiSiApi;
import com.gank.gankly.network.service.BaiSiService;
import com.gank.gankly.utils.DateUtils;

import java.util.Date;

import rx.Observable;

/**
 * Create by LingYan on 2016-11-30
 * Email:137387869@qq.com
 */

public class BaiSiDataSource extends BaseDataSourceModel {
    @Nullable
    private static BaiSiDataSource INSTANCE = null;
    private BaiSiService mGankService;

    private BaiSiDataSource() {
        mGankService = BaiSiApi.getInstance().getService();
    }

    public static BaiSiDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (BaiSiDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BaiSiDataSource();
                }
            }
        }
        return INSTANCE;
    }

    //查询的类型，默认全部返回。
    //type=10 图片
    //type=29 段子
    //type=31 声音
    //type=41 视频
    public Observable<BaiSiBean> fetchData(int page, String type) {
        String time = DateUtils.getFormatDate(new Date(), "yyyyMMddHHmmss");
        return toObservable(mGankService.fetchBaiSi("28004", "0df2dbc6758b4089a60bd7b8a437414d", time, "md5", "0", type, "", page));
    }

    public Observable<BuDeJieBean> fetchImage(int np) {
        return toObservable(mGankService.fetchImage(np, "xiaomi", "6.6.1", "", "6.0.1", "baisibudejie", "android", "866333021430895", "02:00:00:00:00:00", "MI 4LTE", "移动", "1080", "1920", "CN"));
    }

    public Observable<BuDeJieVideo> fetchVideo(int np) {
        return toObservable(mGankService.fetchVideo(np));
    }
}
