package com.gank.gankly.ui.baisi.image;

import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.gank.gankly.R;
import com.gank.gankly.bean.GallerySize;
import com.gank.gankly.ui.base.fragment.SupportFragment;
import com.socks.library.KLog;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;

/**
 * Create by LingYan on 2016-12-05
 */

public class BaiSiGalleryFragment extends SupportFragment {
    private static final String IMAGE_GIF = ".gif";
    public static final String URL = "BaiSi_Url";
    public static final String SIZE_HEIGHT = "Height";
    public static final String SIZE_WIDTH = "Width";

    @BindView(R.id.largetImageview)
    SubsamplingScaleImageView sliderIv;
    @BindView(R.id.baisi_gallery_img)
    ImageView mImageView;

    private String mUrl;
    private int mHeight;
    private int mWidth;

    public static BaiSiGalleryFragment newInstance(GallerySize gallerySize) {
        Bundle args = new Bundle();
        args.putString(URL, gallerySize.getUrl());
        args.putInt(SIZE_HEIGHT, gallerySize.getHeight());
        args.putInt(SIZE_WIDTH, gallerySize.getWidth());
        BaiSiGalleryFragment fragment = new BaiSiGalleryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_baisi_gallery;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sliderIv.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
        sliderIv.setDoubleTapZoomStyle(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mUrl = bundle.getString(URL);
            mWidth = bundle.getInt(SIZE_WIDTH);
            mHeight = bundle.getInt(SIZE_HEIGHT);
        }
        KLog.d("mUrl:" + mUrl);
        loadImageBitmap(mUrl);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void loadImageBitmap(final String url) {
        if (url.endsWith(IMAGE_GIF)) {
            sliderIv.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .asGif()
                    .load(mUrl)
//                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(mImageView);
        } else {
            ViewGroup.LayoutParams layoutParams = sliderIv.getLayoutParams();
            layoutParams.width = 1080;
            sliderIv.setLayoutParams(layoutParams);

            Glide.with(getContext()).load(mUrl).downloadOnly(new SimpleTarget<File>() {
                @Override
                public void onResourceReady(File resource, Transition<? super File> transition) {
                    //设置图片初始化状态 = 从顶部开始加载
                    ImageViewState state = new ImageViewState(0, new PointF(0, 0), 0);
                    if (mHeight < 1920 || mWidth > 1080) {
                        mImageView.setImageURI(Uri.fromFile(resource));
                    } else {
                        mImageView.setVisibility(View.GONE);
                        sliderIv.setVisibility(View.VISIBLE);
                        sliderIv.setImage(ImageSource.uri(Uri.fromFile(resource).getPath()), state);
                    }
                }
            });
        }
    }
}
