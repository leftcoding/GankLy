package com.gank.gankly.mvp.source.remote;

import androidx.annotation.Nullable;

/**
 * Create by LingYan on 2016-10-26
 */

public class MeiZiTuDataSource  {
    @Nullable
    private static MeiZiTuDataSource mInstance = null;

    public static MeiZiTuDataSource getInstance() {
        if (mInstance == null) {
            synchronized (MeiZiTuDataSource.class) {
                if (mInstance == null) {
                    mInstance = new MeiZiTuDataSource();
                }
            }
        }
        return mInstance;
    }
}

