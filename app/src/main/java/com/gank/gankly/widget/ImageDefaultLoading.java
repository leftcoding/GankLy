package com.gank.gankly.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gank.gankly.R;

/**
 * Create by LingYan on 2017-04-28
 * Email:137387869@qq.com
 */

public class ImageDefaultLoading extends LinearLayout {
    private View mView;
    private HorizontalLoading mHorizontalLoading;
    private View mLoading;
    private TextView txtClickLoad;
    private ImageView mRatioImageView;
    private Context mContext;

    //    private ImageView mImageView;
    private String imgUrl;

    private int w, h;

    public ImageDefaultLoading(Context context) {
        this(context, null);
    }

    public ImageDefaultLoading(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(HORIZONTAL);
        mContext = context;
        mView = LayoutInflater.from(context).inflate(R.layout.view_image_default_loading, this, true);
        mHorizontalLoading = (HorizontalLoading) mView.findViewById(R.id.image_default_horizontal_loading);
        txtClickLoad = (TextView) mView.findViewById(R.id.image_default_txt_click_load);
        mLoading = mView.findViewById(R.id.image_default_ll_loading);
        mRatioImageView = (ImageView) mView.findViewById(R.id.image_default_ratio);
    }

    public void toLoading() {
        txtClickLoad.setVisibility(View.GONE);
        mLoading.setVisibility(View.VISIBLE);
        mHorizontalLoading.onStart();

        if (imgUrl != null && mRatioImageView != null) {
            LayoutParams l = (LayoutParams) mRatioImageView.getLayoutParams();
            l.width = w;
            l.height = h;
            mRatioImageView.setLayoutParams(l);
//            GlideUtils.wifiRequest(mContext, false)
            Glide.with(mContext)
                    .load(imgUrl)
                    .error(R.drawable.item_default_img)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            mLoading.postDelayed(() -> {
                                mLoading.setVisibility(View.GONE);
                                mRatioImageView.setVisibility(View.VISIBLE);
                            }, 500);

                            return false;
                        }
                    })
                    .into(mRatioImageView);
        }
    }

    public void setImageView(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
    }
}
