package com.gank.gankly.config.glide;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.gank.gankly.utils.FileUtils;
import com.gank.gankly.utils.gilde.WifiOnlyLoader;

import java.io.InputStream;

@GlideModule
public class MyGlideModule extends AppGlideModule {
    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        registry.append(GlideUrl.class, InputStream.class, new WifiOnlyLoader.Factory(context));
    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
        String diskCache = FileUtils.getGlideDefaultPath(context);
        builder.setDiskCache(new DiskLruCacheFactory(diskCache, 100 * 1024 * 1024));
        builder.setDefaultRequestOptions(new RequestOptions()
                .format(DecodeFormat.PREFER_ARGB_8888)
        );
        builder.setLogLevel(Log.DEBUG);
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}