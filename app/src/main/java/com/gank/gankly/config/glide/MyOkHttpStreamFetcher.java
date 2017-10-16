package com.gank.gankly.config.glide;

import com.bumptech.glide.Priority;
import com.bumptech.glide.integration.okhttp3.OkHttpStreamFetcher;
import com.bumptech.glide.load.model.GlideUrl;

import java.io.InputStream;

import okhttp3.Call;

/**
 * Create by LingYan on 2017-08-23
 */

public class MyOkHttpStreamFetcher extends OkHttpStreamFetcher {
    public MyOkHttpStreamFetcher(Call.Factory client, GlideUrl url) {
        super(client, url);
    }

    @Override
    public void loadData(Priority priority, DataCallback<? super InputStream> callback) {
        super.loadData(priority, callback);
    }
}
