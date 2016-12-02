package com.gank.gankly.mvp.source.remote;

import android.support.annotation.Nullable;

import com.gank.gankly.bean.BaiSiBean;
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
    public Observable<BaiSiBean> fetchVideo(int page) {
        String time = DateUtils.getFormatDate(new Date(), "yyyyMMddHHmmss");
        return toObservable(mGankService.fetchBaiSi("28004", "0df2dbc6758b4089a60bd7b8a437414d", time, "md5", "0", "41", "", page));
    }
}
