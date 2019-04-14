package com.gank.gankly.widget;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gank.gankly.R;
import com.gank.gankly.utils.gilde.ImageLoaderUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2017-04-28
 */

public class ImageDefaultView extends LinearLayout {
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
    @BindView(R.id.image_default_frame)
    FrameLayout mFrameLayout;

    private ImageLoaderUtil mImageLoaderUtil;
    private Context mContext;

    private String imgUrl;
    private int w, h;
    private int loadType = NORMAL;
    private boolean isCanLoad = true;

    public ImageDefaultView(Context context) {
        this(context, null);
    }

    public ImageDefaultView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(HORIZONTAL);
        mImageLoaderUtil = new ImageLoaderUtil();
        mContext = context;
        View mView = LayoutInflater.from(context).inflate(R.layout.view_image_default, this, true);
        ButterKnife.bind(this, mView);
    }

    public void setViewVisible(int loading, int img, int tip) {
        mLoading.setVisibility(loading);
        mFrameLayout.setVisibility(img);
        txtTip.setVisibility(tip);
    }

    public void setFrameLayout(View view) {
        mFrameLayout.removeAllViews();
        FrameLayout.LayoutParams tparams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);//定义显示组件参数
        mFrameLayout.addView(view, tparams);
    }

    public void showLoadText() {
        showText("点击加载图片");
    }

    public void showErrorText() {
        showText("加载失败，重新加载");
    }

    public void showText(String str) {
        isCanLoad = true;
        mHorizontalLoading.endAnimator();
        setViewVisible(View.GONE, View.GONE, View.VISIBLE);
        txtTip.setText(str);
    }

    public void showLoading() {
        isCanLoad = false;
        mHorizontalLoading.onStart();
        setViewVisible(View.VISIBLE, View.GONE, View.GONE);
    }

    public void showImage() {
        isCanLoad = false;
        mHorizontalLoading.endAnimator();
        setViewVisible(View.GONE, View.VISIBLE, View.GONE);
    }

    public boolean isCanLoad() {
        return isCanLoad;
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
