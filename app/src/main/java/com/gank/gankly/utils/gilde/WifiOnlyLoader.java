package com.gank.gankly.utils.gilde;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.load.model.stream.HttpGlideUrlLoader;
import com.gank.gankly.utils.GanklyPreferences;

import java.io.InputStream;

import static com.gank.gankly.config.Preferences.SETTING_WIFI_ONLY;

public class WifiOnlyLoader implements ModelLoader<GlideUrl, InputStream> {
    private final ModelLoader<GlideUrl, InputStream> defaultLoader;

    public WifiOnlyLoader(ModelLoader<GlideUrl, InputStream> loader) {
        defaultLoader = loader;
    }

    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(GlideUrl glideUrl, int width, int height, Options options) {
        SharedPreferences prefs = GanklyPreferences.getDefaultPreference();
        if (prefs.getBoolean(SETTING_WIFI_ONLY, true)) {
            return new NetworkDisablingLoader().buildLoadData(glideUrl, width, height, options);
        } else {
            return defaultLoader.buildLoadData(glideUrl, width, height, options);
        }
    }

    @Override
    public boolean handles(GlideUrl glideUrl) {
        return false;
    }

    public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {
        @Override
        public ModelLoader<GlideUrl, InputStream> build(MultiModelLoaderFactory multiFactory) {
            return new WifiOnlyLoader(new HttpGlideUrlLoader(new ModelCache<>(500)));
        }

        @Override
        public void teardown() {
        }
    }
}