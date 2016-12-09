package com.gank.gankly.ui.baisi.image;

import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.gank.gankly.R;
import com.gank.gankly.mvp.base.BaseFragment;
import com.socks.library.KLog;

import java.io.File;

import butterknife.BindView;

/**
 * Create by LingYan on 2016-12-05
 * Email:137387869@qq.com
 */

public class BaiSiGalleryFragment extends BaseFragment {
    public static final String URL = "BaiSi_Url";
    public static final int MAX_IMAGE_WIDTH = 4096;
    public static final int MAX_IMAGE_HEIGHT = 4096;

    @BindView(R.id.largetImageview)
    SubsamplingScaleImageView sliderIv;
    @BindView(R.id.baisi_gallery_img)
    ImageView mImageView;

    private String mUrl;

    public static BaiSiGalleryFragment newInstance(String url) {

        Bundle args = new Bundle();
        args.putString(URL, url);
        BaiSiGalleryFragment fragment = new BaiSiGalleryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_baisi_gallery;
    }

    @Override
    protected void initValues() {
        sliderIv.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
        sliderIv.setDoubleTapZoomStyle(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
//        sliderIv.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
//        sliderIv.setMinScale(0.0F);
//        sliderIv.setMaxScale(3.0F);//必须设置
        Bundle bundle = getArguments();
        if (bundle != null) {
            mUrl = bundle.getString(URL);
        }
//        getActivity().supportStartPostponedEnterTransition();
        loadImageBitmap(mUrl);
    }

    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        getActivity().supportStartPostponedEnterTransition();
                        return true;
                    }
                });
    }

    public void loadImageBitmap(final String url) {
        if (url.endsWith(".gif")) {
            sliderIv.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
            KLog.d("gif" + "url:" + url);
            Glide.with(this)
                    .load(mUrl)
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE) // gif must be add this
                    .into(mImageView);
        } else if (url.endsWith(".png") || url.endsWith(".jpg")) {
            KLog.d("jpg");
            mImageView.setVisibility(View.GONE);
            sliderIv.setVisibility(View.VISIBLE);
            Glide.with(this).load(mUrl).downloadOnly(new SimpleTarget<File>() {
                @Override
                public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                    //设置图片初始化状态 = 从顶部开始加载
                    ImageViewState state = new ImageViewState(0, new PointF(0, 0), 0);
                    sliderIv.setImage(ImageSource.uri(Uri.fromFile(resource).getPath()), state);
                }
            });
        }
//        Glide.with(getActivity())
//                .load(url)
//                .asBitmap()
//                .fitCenter()
//                .dontAnimate()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .priority(Priority.IMMEDIATE)
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        File file = new File(FileUtils.getGlideDefaultPath(getContext()), System.currentTimeMillis() + ".jpg");
//                        if (!file.exists()) {
//                            try {
//                                file.createNewFile();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//                            FileOutputStream fout = null;
//                            try {
//                                //保存图片
//                                fout = new FileOutputStream(file);
//                                resource.compress(Bitmap.CompressFormat.JPEG, 100, fout);
//                                // 将保存的地址给SubsamplingScaleImageView,这里注意设置ImageViewState
//
//                                sliderIv.setImage(ImageSource.uri(file.getAbsolutePath()), new ImageViewState(0F, new PointF(0, 0), 0));
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            } finally {
//                                try {
//                                    if (fout != null) fout.close();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        } else {
//
//                        }
//                    }
//                });

    }

    private void setCardViewLayoutParams(ImageView mImageView, int width, int height) {
        ViewGroup.LayoutParams layoutParams = mImageView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        mImageView.setLayoutParams(layoutParams);
    }


    @Override
    protected void initViews() {

    }

    @Override
    protected void bindLister() {

    }
}
