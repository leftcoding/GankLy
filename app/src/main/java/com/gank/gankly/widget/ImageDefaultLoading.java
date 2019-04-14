package com.gank.gankly.widget;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gank.gankly.R;
import com.gank.gankly.utils.gilde.ImageLoaderUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2017-04-28
 */

public class ImageDefaultLoading extends LinearLayout {
    private static final int NORMAL = 0;
    private static final int LOADING = 1;
    private static final int OVER = 2;
    private static final int FAILED = 3;

    @BindView(R.id.image_default_horizontal_loading)
    HorizontalLoading mHorizontalLoading;
    @BindView(R.id.image_default_ll_loading)
    View mLoading;
    @BindView(R.id.image_default_txt_click_load)
    TextView txtTip;
    @BindView(R.id.image_default_ratio)
    ImageView mImageView;
    @BindViews({R.id.image_default_ll_loading, R.id.image_default_txt_click_load, R.id.image_default_ratio})
    List<View> mViews;

    private ImageLoaderUtil mImageLoaderUtil;
    private Context mContext;

    private String imgUrl;
    private int w, h;
    private int loadType = NORMAL;

    public ImageDefaultLoading(Context context) {
        this(context, null);
    }

    public ImageDefaultLoading(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(HORIZONTAL);
        mImageLoaderUtil = new ImageLoaderUtil();
        mContext = context;
        View mView = LayoutInflater.from(context).inflate(R.layout.view_image_default_loading, this, true);
        ButterKnife.bind(this, mView);
    }

    public ImageView getImageView() {
        return mImageView;
    }

    public void setViewVisible(int loading, int img, int tip) {
        mLoading.setVisibility(loading);
        mImageView.setVisibility(img);
        txtTip.setVisibility(tip);
    }

    public void showLoadText() {
        showText("点击加载图片");
    }

    public void showErrorText() {
        showText("加载失败，重新加载");
    }

    public void showText(String str) {
        mHorizontalLoading.endAnimator();
        setViewVisible(View.GONE, View.GONE, View.VISIBLE);
        txtTip.setText(str);
    }

    public void showLoading() {
        mHorizontalLoading.onStart();
        setViewVisible(View.VISIBLE, View.GONE, View.GONE);
    }

    public void showImage() {
        mHorizontalLoading.endAnimator();
        setViewVisible(View.GONE, View.VISIBLE, View.GONE);
    }

    public Context getImageContext() {
        return mContext;
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
