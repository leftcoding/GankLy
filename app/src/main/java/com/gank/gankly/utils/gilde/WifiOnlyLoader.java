package com.gank.gankly.utils.gilde;

import android.content.Context;
import android.content.SharedPreferences;

import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.stream.HttpUrlGlideUrlLoader;
import com.gank.gankly.utils.GanklyPreferences;

import java.io.InputStream;

import static com.gank.gankly.config.Preferences.SETTING_WIFI_ONLY;

public class WifiOnlyLoader implements ModelLoader<GlideUrl, InputStream> {
    private final ModelLoader<GlideUrl, InputStream> defaultLoader;

    public WifiOnlyLoader(ModelLoader<GlideUrl, InputStream> loader) {
        defaultLoader = loader;
    }

    @Override
    public DataFetcher<InputStream> getResourceFetcher(GlideUrl model, int width, int height) {
        SharedPreferences prefs = GanklyPreferences.getDefaultPreference();
        if (prefs.getBoolean(SETTING_WIFI_ONLY, true)) {
            return new NetworkDisablingFetcher(model);
        } else {
            return defaultLoader.getResourceFetcher(model, width, height);
        }
    }

    public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {
        @Override
        public ModelLoader<GlideUrl, InputStream> build(Context context, GenericLoaderFactory factories) {
            //ModelLoader<GlideUrl, InputStream> loader = factories.buildModelLoader(GlideUrl.class, InputStream.class);
            // the above could be used when you have a custom model, this version already replaced the default loader,
            // so it needs to be created explicitly. If you use OkHttp or Volley create that.
            return new WifiOnlyLoader(new HttpUrlGlideUrlLoader(new ModelCache<GlideUrl, GlideUrl>(500)));
        }

        @Override
        public void teardown() {
        }
    }
}