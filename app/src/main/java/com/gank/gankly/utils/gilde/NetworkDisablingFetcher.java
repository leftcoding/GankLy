package com.gank.gankly.utils.gilde;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;

import java.io.IOException;
import java.io.InputStream;

public class NetworkDisablingFetcher implements DataFetcher<InputStream> {
    private final Object model;

    public NetworkDisablingFetcher(Object model) {
        this.model = model;
    }

    @Override
    public InputStream loadData(Priority priority) throws Exception {
        throw new IOException("Fake network error");
    }

    @Override
    public void cleanup() {
    }

    @Override
    public String getId() {
        return model.toString();
    }

    @Override
    public void cancel() {
    }
}