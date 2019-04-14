package com.gank.gankly.utils.gilde;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;

import java.io.InputStream;

public class NetworkDisablingLoader implements ModelLoader<GlideUrl, InputStream> {
    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(GlideUrl t, int width, int height, Options options) {
        return new LoadData<>(t, new NetworkDisablingFetcher(t));
    }

    @Override
    public boolean handles(GlideUrl file) {
        return false;
    }
}