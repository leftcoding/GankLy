package com.gank.gankly.config.glide;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

import java.io.InputStream;

import androidx.annotation.Nullable;
import okhttp3.OkHttpClient;

/**
 * A simple model loader for fetching media over http/https using OkHttp.
 */
public class MyOkHttpUrlLoader implements ModelLoader<GlideUrl, InputStream> {
    private final OkHttpClient mOkHttpClient;

    public MyOkHttpUrlLoader(OkHttpClient okHttpClient) {
        this.mOkHttpClient = okHttpClient;
    }

    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(GlideUrl glideUrl, int width, int height, Options options) {
        return new LoadData<>(glideUrl, buildResourceFetcher(glideUrl, width, height, options));
    }

    @Override
    public boolean handles(GlideUrl glideUrl) {
        return false;
    }

    private DataFetcher<InputStream> buildResourceFetcher(GlideUrl model, int width, int height, Options options) {
        return new MyOkHttpStreamFetcher(mOkHttpClient, model);
    }

    /**
     * The default factory for {@link MyOkHttpUrlLoader}s.
     */
    public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {
        private final OkHttpClient mOkHttpClient;

        /**
         * Constructor for a new Factory that runs requests using a static singleton client.
         */
        public Factory(OkHttpClient okHttpClient) {
            this.mOkHttpClient = okHttpClient;
        }

        @Override
        public ModelLoader<GlideUrl, InputStream> build(MultiModelLoaderFactory multiFactory) {
            return new MyOkHttpUrlLoader(mOkHttpClient);
        }

        @Override
        public void teardown() {
            // Do nothing, this instance doesn't own the client.
        }
    }
}
