package com.gank.gankly.utils.gilde;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gank.gankly.R;

import java.io.IOException;
import java.io.InputStream;


/**
 * Created by Anthony on 2016/3/3.
 * Class Note:
 * using {@link Glide} to load image
 */
public class GlideImageLoaderStrategy implements BaseImageLoaderStrategy {

    @Override
    public void loadImage(String url, ImageView imageView) {
        Glide.with(imageView.getContext()).load(url)
                .placeholder(imageView.getDrawable())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
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
    public DrawableRequestBuilder<String> loadWifiImage(Context context, String url, boolean isWifi, boolean isOnlyWifi) {
        boolean isCache = false;
        DrawableRequestBuilder<String> requestBuilder;
        if (isOnlyWifi) {
            if (!isWifi) {
                isCache = true;
            }
        }

        if (!isCache) {
            requestBuilder = Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL);
        } else {
            requestBuilder = Glide.with(context)
                    .using(new StreamModelLoader<String>() {
                        @Override
                        public DataFetcher<InputStream> getResourceFetcher(final String s, int i, int i1) {
                            return new DataFetcher<InputStream>() {
                                @Override
                                public InputStream loadData(Priority priority) throws Exception {
                                    throw new IOException("Download not allowed");
                                }

                                @Override
                                public void cleanup() {

                                }

                                @Override
                                public String getId() {
                                    return s;
                                }

                                @Override
                                public void cancel() {

                                }
                            };
                        }
                    })
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
        }
        return requestBuilder;
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
    public void loadImageCall(String url, ImageView imageView, int placeholder, RequestListener<String, GlideDrawable> listener) {
        Glide.with(imageView.getContext()).load(url)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(R.drawable.ic_bottom_bar_more)
                .listener(listener)
                .centerCrop()
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
        DrawableTypeRequest<String> drawableTypeRequest = Glide.with(ctx).load(url);
        if (placeholder != 0) {
            drawableTypeRequest.placeholder(placeholder);
        }

        drawableTypeRequest.diskCacheStrategy(DiskCacheStrategy.SOURCE);
        drawableTypeRequest.listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                return false;
            }
        })      .fitCenter()
                .into(imageView);
    }
}
