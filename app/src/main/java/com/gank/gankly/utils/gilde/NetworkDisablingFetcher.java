package com.gank.gankly.utils.gilde;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;

import java.io.InputStream;

public class NetworkDisablingFetcher implements DataFetcher<InputStream> {
    private final GlideUrl model;

    public NetworkDisablingFetcher(GlideUrl model) {
        this.model = model;
    }

    @Override
    public void loadData(Priority priority, DataCallback<? super InputStream> callback) {

    }

    @Override
    public void cleanup() {

    }

    @Override
    public void cancel() {

    }

    @Override
    public Class<InputStream> getDataClass() {
        return null;
    }

    @Override
    public DataSource getDataSource() {
        return null;
    }
}