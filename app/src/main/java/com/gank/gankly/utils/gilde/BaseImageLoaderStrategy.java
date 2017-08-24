package com.gank.gankly.utils.gilde;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestListener;

/**
 */
public interface BaseImageLoaderStrategy {
    //无占位图
    void loadImage(String url, ImageView imageView);

    //这里的context指定为ApplicationContext
    void loadImageWithAppCxt(String url, ImageView imageView);

    void loadImage(String url, int placeholder, ImageView imageView);

    void loadImage(Context context, String url, int placeholder, ImageView imageView);

    void loadCircleImage(String url, int placeholder, ImageView imageView);

    void loadCircleBorderImage(String url, int placeholder, ImageView imageView, float borderWidth, int borderColor);

    void loadCircleBorderImage(String url, int placeholder, ImageView imageView, float borderWidth, int borderColor, int heightPx, int widthPx);

    void loadImageCall(String url, ImageView imageView, int placeholder, final RequestListener<Drawable> listener);

    void loadGifImage(String url, int placeholder, ImageView imageView);

    //清除硬盘缓存
    void clearImageDiskCache(final Context context);

    //清除内存缓存
    void clearImageMemoryCache(Context context);

    //根据不同的内存状态，来响应不同的内存释放策略
    void trimMemory(Context context, int level);

    //获取缓存大小
    String getCacheSize(Context context);

    void saveImage(Context context, String url, String savePath, String saveFileName, ImageSaveListener listener);

    RequestBuilder<Drawable> loadWifiImage(Context context, String url);

    RequestBuilder<Drawable> loadManualImage(Context context, String url);

    RequestBuilder<Bitmap> loadAsImage(Context context, String url);

    RequestBuilder<Bitmap> glideAsBitmap(Context context, String imgUrl);
}
