package com.gank.gankly.ui.welfare;

import android.content.Context;
import android.graphics.Bitmap;
import android.ly.business.domain.Gank;
import androidx.annotation.Nullable;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gank.gankly.R;
import com.gank.gankly.ui.base.adapter.BaseViewModel;
import com.gank.gankly.ui.base.adapter.ButterKnifeHolder;
import com.gank.gankly.utils.AppUtils;
import com.gank.gankly.utils.gilde.ImageLoaderUtil;
import com.gank.gankly.widget.ImageDefaultView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2018-09-25
 */
public class ImageViewModel extends BaseViewModel<ImageViewModel.ViewHolder> {
    private ArrayMap<String, Integer> heights = new ArrayMap<>();

    private Gank gank;

    private WelfareViewManager.ItemClickListener itemClickListener;

    ImageViewModel() {

    }

    public void setData(Gank gank) {
        this.gank = gank;
    }

    public void setListener(WelfareViewManager.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private void setCardViewLayoutParams(ImageDefaultView mImageView, int width, int height) {
        ViewGroup.LayoutParams layoutParams = mImageView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        mImageView.setLayoutParams(layoutParams);
    }

    @Override
    public ViewHolder createViewHolder(ViewGroup parent) {
        return new ViewHolder(createView(parent));
    }

    @Override
    public void bindView(final ViewHolder viewHolder) {
        super.bindView(viewHolder);
        final String url = gank.url;

        RequestBuilder<Bitmap> requestBuilder = ImageLoaderUtil.getInstance()
                .glideAsBitmap(context, url);

        if (heights.containsKey(url)) {
            setCardViewLayoutParams(viewHolder.imgMeizi, viewHolder.screenWidth, heights.get(url));
            requestBuilder.apply(new RequestOptions()
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
            );
            requestBuilder.into(viewHolder.imageView);
        } else {
            requestBuilder.apply(new RequestOptions()
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
                    .override(viewHolder.screenWidth, viewHolder.screenHeight)//设置宽高一致，后期改动不大
            );
            requestBuilder.into(new DriverViewTarget(viewHolder.imageView, new onResourceCallback() {
                @Override
                public void onCallback(int width, int height) {
                    if (!heights.containsKey(url) && url != null) {
                        int viewHeight = width * viewHolder.screenWidth / height;
                        heights.put(url, viewHeight);
                        setCardViewLayoutParams(viewHolder.imgMeizi, viewHolder.screenWidth, heights.get(url));
                    }
                }
            }));
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItem(viewHolder.imgMeizi, viewHolder.getLayoutPosition());
            }
        });
    }

    @Override
    public int getLayoutRes() {
        return R.layout.adapter_meizi;
    }

    public interface onResourceCallback {
        void onCallback(int width, int height);
    }

    private static class DriverViewTarget extends BitmapImageViewTarget {
        private final onResourceCallback resourceCallback;

        DriverViewTarget(ImageView image, onResourceCallback resourceCallback) {
            super(image);
            this.resourceCallback = resourceCallback;
        }

        @Override
        public void onResourceReady(Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
            super.onResourceReady(resource, transition);
            if (resourceCallback != null) {
                resourceCallback.onCallback(resource.getWidth(), resource.getHeight());
            }
        }
    }

    public static class ViewHolder extends ButterKnifeHolder {
        @BindView(R.id.img_meizi)
        ImageDefaultView imgMeizi;

        @BindView(R.id.meizi_card_view)
        RelativeLayout mRelativeLayout;

        ImageView imageView;

        private int screenWidth;
        private int screenHeight;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
            final Context context = view.getContext();
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imgMeizi.setFrameLayout(imageView);

            screenWidth = screenHeight = AppUtils.getDisplayWidth(context) / 2;
        }
    }
}
