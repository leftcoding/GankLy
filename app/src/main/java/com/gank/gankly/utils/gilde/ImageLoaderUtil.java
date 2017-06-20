package com.gank.gankly.utils.gilde;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;


/**
 * Created by soulrelay on 2016/10/11 13:42.
 * Class Note:
 * use this class to load image,single instance
 */
public class ImageLoaderUtil {

    //图片默认加载类型 以后有可能有多种类型
    public static final int PIC_DEFAULT_TYPE = 0;

    //图片默认加载策略 以后有可能有多种图片加载策略
    public static final int LOAD_STRATEGY_DEFAULT = 0;

    private static ImageLoaderUtil mInstance;
    //本应该使用策略模式，用基类声明，但是因为Glide特殊问题
    //持续优化更新
    private BaseImageLoaderStrategy mStrategy;

    public ImageLoaderUtil() {
        mStrategy = new GlideImageLoaderStrategy();
    }

    //单例模式，节省资源
    public static ImageLoaderUtil getInstance() {
        if (mInstance == null) {
            synchronized (ImageLoaderUtil.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoaderUtil();
                    return mInstance;
                }
            }
        }
        return mInstance;
    }

    public void loadImage(String url, int placeholder, ImageView imageView) {
        mStrategy.loadImage(imageView.getContext(), url, placeholder, imageView);
    }

    public void loadGifImage(String url, int placeholder, ImageView imageView) {
        mStrategy.loadGifImage(url, placeholder, imageView);
    }

    public void loadCircleImage(String url, int placeholder, ImageView imageView) {
        mStrategy.loadCircleImage(url, placeholder, imageView);
    }

    public void loadCircleBorderImage(String url, int placeholder, ImageView imageView, float borderWidth, int borderColor) {
        mStrategy.loadCircleBorderImage(url, placeholder, imageView, borderWidth, borderColor);
    }

    public void loadCircleBorderImage(String url, int placeholder, ImageView imageView, float borderWidth, int borderColor, int heightPX, int widthPX) {
        mStrategy.loadCircleBorderImage(url, placeholder, imageView, borderWidth, borderColor, heightPX, widthPX);
    }

    public void loadImageCall(String url, ImageView imageView, int placeholder, final RequestListener<String, GlideDrawable> listener) {
        mStrategy.loadImageCall(url, imageView, placeholder, listener);
    }

    public void loadImageWithAppCxt(String url, ImageView imageView) {
        mStrategy.loadImageWithAppCxt(url, imageView);
    }

    public DrawableRequestBuilder<String> loadWifiImage(Context context, String url) {
        return mStrategy.loadWifiImage(context, url);
    }

    public DrawableRequestBuilder<String> loadManualImage(Context context, String url) {
        return mStrategy.loadManualImage(context, url);
    }

    public BitmapRequestBuilder<String, Bitmap> loadAsImage(Context context, String url) {
        return mStrategy.loadAsImage(context, url);
    }

    public BitmapRequestBuilder<String, Bitmap> glideAsBitmap(Context context, String url) {
        return mStrategy.glideAsBitmap(context, url);
    }

    /**
     * 策略模式的注入操作
     *
     * @param strategy
     */
    public void setLoadImgStrategy(BaseImageLoaderStrategy strategy) {
        mStrategy = strategy;
    }

    /**
     * 需要展示图片加载进度的请参考 GalleryAdapter
     * 样例如下所示
     */

    /**
     * 清除图片磁盘缓存
     */
    public void clearImageDiskCache(final Context context) {
        mStrategy.clearImageDiskCache(context);
    }

    /**
     * 清除图片内存缓存
     */
    public void clearImageMemoryCache(Context context) {
        mStrategy.clearImageMemoryCache(context);
    }

    /**
     * 根据不同的内存状态，来响应不同的内存释放策略
     *
     * @param context
     * @param level
     */
    public void trimMemory(Context context, int level) {
        mStrategy.trimMemory(context, level);
    }

    /**
     * 清除图片所有缓存
     */
    public void clearImageAllCache(Context context) {
        clearImageDiskCache(context.getApplicationContext());
        clearImageMemoryCache(context.getApplicationContext());
    }

    /**
     * 获取缓存大小
     *
     * @return CacheSize
     */
    public String getCacheSize(Context context) {
        return mStrategy.getCacheSize(context);
    }

    public void saveImage(Context context, String url, String savePath, String saveFileName, ImageSaveListener listener) {
        mStrategy.saveImage(context, url, savePath, saveFileName, listener);
    }
}
