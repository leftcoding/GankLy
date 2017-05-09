package com.gank.gankly.widget;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.gank.gankly.R;
import com.gank.gankly.config.Preferences;
import com.gank.gankly.utils.GanklyPreferences;
import com.gank.gankly.utils.NetworkUtils;
import com.gank.gankly.utils.gilde.GlideUtils;
import com.socks.library.KLog;

import java.io.File;

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
            GlideUtils.wifiRequest(mContext, false)
//            Glide.with(mContext)
                    .load(imgUrl)
                    .error(R.drawable.item_default_img)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            KLog.e(e + ",model:" + model);
                            Glide.with(mContext).load(imgUrl)
                                    .error(R.drawable.item_default_img);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            KLog.d("model:" + model);
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

    public ImageDefaultLoading setImageView(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    private void loadNormal() {
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
                        return false;
                    }
                })
                .into(mRatioImageView);
    }

    public void onStart() {
        if (!disLoading()) {
            txtClickLoad.setVisibility(View.GONE);
            mRatioImageView.setVisibility(View.VISIBLE);
            loadNormal();
        } else {
            Glide.with(mContext).load(imgUrl).downloadOnly(new SimpleTarget<File>() {
                @Override
                public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                    String path = Uri.fromFile(resource).getPath();
                    KLog.d("path:" + path);
                    if (!TextUtils.isEmpty(path)) {
                        txtClickLoad.setVisibility(View.GONE);
                        mLoading.setVisibility(View.GONE);
                        mRatioImageView.setVisibility(View.VISIBLE);
                        mRatioImageView.setImageURI(Uri.fromFile(resource));
                    } else {
                        txtClickLoad.setVisibility(View.VISIBLE);
                        mLoading.setVisibility(View.GONE);
                        mRatioImageView.setVisibility(View.GONE);
                    }
                }
            });

//            GlideUtils.wifiRequest(mContext, true)
//                    .load(imgUrl)
//                    .error(R.drawable.item_default_img)
//                    .fitCenter()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .listener(new RequestListener<String, GlideDrawable>() {
//                        @Override
//                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            KLog.e(e + ",model:" + model);
//                            txtClickLoad.setVisibility(View.VISIBLE);
//                            mLoading.setVisibility(View.GONE);
//                            mRatioImageView.setVisibility(View.GONE);
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            KLog.d("model:" + model);
//                            mLoading.postDelayed(() -> {
//                                txtClickLoad.setVisibility(View.GONE);
//                                mLoading.setVisibility(View.GONE);
//                                mRatioImageView.setVisibility(View.VISIBLE);
//                            }, 500);
//
//                            return false;
//                        }
//                    })
//                    .into(mRatioImageView);
        }
    }

    private boolean disLoading() {
        return GanklyPreferences.getBoolean(Preferences.SETTING_WIFI_ONLY, false) && NetworkUtils.isMobileNetwork();
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
