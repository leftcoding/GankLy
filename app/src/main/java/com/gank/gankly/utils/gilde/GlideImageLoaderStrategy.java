package com.gank.gankly.utils.gilde;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.gank.gankly.R;
import com.gank.gankly.config.Preferences;
import com.gank.gankly.utils.GanklyPreferences;
import com.gank.gankly.utils.NetworkUtils;


/**
 */
public class GlideImageLoaderStrategy implements BaseImageLoaderStrategy {

    @Override
    public void loadImage(String url, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(url)
                .apply(new RequestOptions()
                        .placeholder(imageView.getDrawable())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(imageView);
    }

    @Override
    public void loadImageWithAppCxt(String url, ImageView imageView) {

    }

    @Override
    public void loadImage(String url, int placeholder, ImageView imageView) {
        loadNormal(imageView.getContext(), url, placeholder, imageView);
    }

    @Override
    public void loadImage(Context context, String url, int placeholder, ImageView imageView) {
        loadNormal(context, url, placeholder, imageView);
    }

    @Override
    public RequestBuilder<Drawable> loadWifiImage(Context context, String url) {
        return getImageCache(context, url, isAllowDown(context));
    }

    @Override
    public RequestBuilder<Drawable> loadManualImage(Context context, String url) {
        return getImageCache(context, url, true);
    }

    @Override
    public RequestBuilder<Bitmap> loadAsImage(Context context, String url) {
        return Glide.with(context).asBitmap().load(url).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE));
    }

    @Override
    public void loadCircleImage(String url, int placeholder, ImageView imageView) {

    }

    @Override
    public void loadCircleBorderImage(String url, int placeholder, ImageView imageView, float borderWidth, int borderColor) {

    }

    @Override
    public void loadCircleBorderImage(String url, int placeholder, ImageView imageView, float borderWidth, int borderColor, int heightPx, int widthPx) {

    }

    @Override
    public void loadImageCall(String url, ImageView imageView, int placeholder, RequestListener<Drawable> listener) {
        Glide.with(imageView.getContext())
                .load(url)
                .apply(new RequestOptions()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .error(R.drawable.ic_bottom_bar_more)
                        .centerCrop()
                )
                .listener(listener)
                .into(imageView);
    }

    @Override
    public void loadGifImage(String url, int placeholder, ImageView imageView) {

    }

    @Override
    public void clearImageDiskCache(Context context) {

    }

    @Override
    public void clearImageMemoryCache(Context context) {

    }

    @Override
    public void trimMemory(Context context, int level) {

    }

    @Override
    public String getCacheSize(Context context) {
        return null;
    }

    @Override
    public void saveImage(Context context, String url, String savePath, String saveFileName, ImageSaveListener listener) {

    }

    @Override
    public RequestBuilder<Bitmap> glideAsBitmap(Context context, String imgUrl) {
        if (isAllowDown(context)) {
            return Glide.with(context)
                    .asBitmap()
                    .load(imgUrl)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE));
        } else {
            return Glide.with(context)
//                    .using(new StreamModelLoader<String>() {
//                        @Override
//                        public DataFetcher<InputStream> getResourceFetcher(final String s, int i, int i1) {
//                            return new DataFetcher<InputStream>() {
//                                @Override
//                                public InputStream loadData(Priority priority) throws Exception {
//                                    throw new IOException("Download not allowed");
//                                }
//
//                                @Override
//                                public void cleanup() {
//
//                                }
//
//                                @Override
//                                public String getId() {
//                                    return s;
//                                }
//
//                                @Override
//                                public void cancel() {
//
//                                }
//                            };
//                        }
//                    })
                    .asBitmap()
                    .load(imgUrl)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    );
        }
    }

    /**
     * load image with Glide
     */
    private void loadNormal(final Context ctx, final String url, int placeholder, ImageView imageView) {
        /**
         *  为其添加缓存策略,其中缓存策略可以为:Source及None,None及为不缓存,Source缓存原型.如果为ALL和Result就不行.然后几个issue的连接:
         https://github.com/bumptech/glide/issues/513
         https://github.com/bumptech/glide/issues/281
         https://github.com/bumptech/glide/issues/600
         modified by xuqiang
         */

        //去掉动画 解决与CircleImageView冲突的问题 这个只是其中的一个解决方案
        //使用SOURCE 图片load结束再显示而不是先显示缩略图再显示最终的图片（导致图片大小不一致变化）
        RequestBuilder<Drawable> drawableTypeRequest = Glide.with(ctx).load(url);
        if (placeholder != 0) {
            drawableTypeRequest.apply(new RequestOptions().placeholder(placeholder));
        }

        drawableTypeRequest.apply(new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .fitCenter()
        );
        drawableTypeRequest.listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        })
                .into(imageView);
    }

    private boolean isAllowDown(Context context) {
        boolean allowDownload = true;
        boolean isOnlyWif = GanklyPreferences.getBoolean(context, Preferences.SETTING_WIFI_ONLY, false);
        if (isOnlyWif) {
            if (!NetworkUtils.isWiFi(context)) {
                allowDownload = false;
            }
        }
        return allowDownload;
    }

    private RequestBuilder<Drawable> getImageCache(Context context, String url, boolean isAllowDown) {
        if (isAllowDown) {
            return Glide.with(context)
                    .load(url)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    );
        } else {
            return Glide.with(context)
                    .load(url)
                    .apply(new RequestOptions()
                            .onlyRetrieveFromCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    );
        }
    }
}
