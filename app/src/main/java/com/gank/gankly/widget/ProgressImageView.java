package com.gank.gankly.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.gank.gankly.R;
import com.gank.gankly.config.glide.ProgressTarget;

import androidx.fragment.app.Fragment;

/**
 * Glide image with progress
 * Create by LingYan on 2016-5-19
 */
public class ProgressImageView extends RelativeLayout {
    private boolean showProgressText = true, showProgressBar = true;
    private TextView progressTextView;
    private TouchImageView imageView;
    private ProgressBar progressBar;
    private ProgressTarget<String, Bitmap> target;
    private ImageViewOnClick mImageViewOnClick;

    public ProgressImageView(Context context) {
        super(context);
        initializeViews(context, null);
    }

    public ProgressImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context, attrs);
    }

    private void initializeViews(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.fragment_image_view, this, true);

        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.ProgressImageView,
                    0, 0);

            try {
                showProgressText = a.getBoolean(R.styleable.ProgressImageView_showProgressText, true);
                showProgressBar = a.getBoolean(R.styleable.ProgressImageView_showProgressBar, true);
            } finally {
                a.recycle();
            }
        }
    }

    public void setImageViewOnClick(ImageViewOnClick imageViewOnClick) {
        mImageViewOnClick = imageViewOnClick;
    }

    public interface ImageViewOnClick {
        void onImageClick(View v);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        imageView = (TouchImageView) getChildAt(0);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageViewOnClick.onImageClick(v);
            }
        });
        progressBar = (ProgressBar) getChildAt(1);
        if (!showProgressBar) progressBar.setVisibility(GONE);
        progressTextView = (TextView) getChildAt(2);
        if (!showProgressText) progressTextView.setVisibility(GONE);

//        target = new MyProgressTarget<>(new GlideDrawableImageViewTarget(imageView), progressBar, imageView, progressTextView);
        target = new MyProgressTarget<>(new BitmapImageViewTarget(imageView), progressBar, progressTextView);
    }

    public void load(String url, final Fragment fragment) {
        target.setModel(url); // update target's cache

        if (url.contains("i.meizitu.net/")) {
            if (url.contains("-")) {
                int point = url.lastIndexOf("-");
                int name = url.lastIndexOf(".");
                String first = url.substring(0, point);
                String end = url.substring(name, url.length());
                url = first + end;
            }
            GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                    .addHeader("referer", "http://www.mzitu.com/mm/")
                    .build());

            Glide.with(fragment.getContext())
                    .asBitmap()
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .placeholder(R.drawable.image_loading)
                            .error(R.drawable.image_failure)
                            .fitCenter()
                    )
                    .load(glideUrl)
                    .into(target);
        } else {
            Glide.with(fragment.getContext())
                    .asBitmap()
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .placeholder(R.drawable.image_loading)
                            .error(R.drawable.image_failure)
                            .fitCenter()
                    )
                    .load(url)
                    .into(target);
        }
    }

    public void load(String url, Activity fragment) {
        target.setModel(url); // update target's cache

        Glide.with(fragment)
                .asBitmap()
                .load(url)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .placeholder(R.drawable.image_loading)
                        .error(R.drawable.image_failure)
                        .fitCenter() // needs explicit transformation, because we're using a custom target
                )
                .into(target);
    }

    /**
     * Demonstrates 3 different ways of showing the progress:
     * <ul>
     * <li>Update a full fledged progress bar</li>
     * <li>Update a text view to display size/percentage</li>
     * <li>Update the placeholder via Drawable.level</li>
     * </ul>
     * This last one is tricky: the placeholder that Glide sets can be used as a progress drawable
     * without any extra Views in the view hierarchy if it supports levels via <code>usesLevel="true"</code>
     * or <code>level-list</code>.
     *
     * @param <Z> automatically match any real Glide target so it can be used flexibly without reimplementing.
     */
    private static class MyProgressTarget<Z> extends ProgressTarget<String, Z> {
        private final TextView text;
        private final ProgressBar progress;

        public MyProgressTarget(Target<Z> target, ProgressBar progress, TextView text) {
            super(target);
            this.progress = progress;
//            this.image = image;
            this.text = text;
        }

        @Override
        public float getGranualityPercentage() {
            return 0.1f; // this matches the format string for #text below
        }

        @Override
        protected void onConnecting() {
            progress.setIndeterminate(true);
            progress.setVisibility(View.VISIBLE);
//            image.setImageLevel(0);
            text.setVisibility(View.VISIBLE);
            text.setText("connecting");
        }

        @Override
        protected void onDownloading(long bytesRead, long expectedLength) {
            progress.setIndeterminate(false);
            progress.setProgress((int) (100 * bytesRead / expectedLength));
//            image.setImageLevel((int) (10000 * bytesRead / expectedLength));
            text.setText(String.format("downloading %.2f/%.2f MB %.1f%%",
                    bytesRead / 1e6, expectedLength / 1e6, 100f * bytesRead / expectedLength));
        }

        @Override
        protected void onDownloaded() {
            progress.setIndeterminate(true);
//            image.setImageLevel(10000);
            text.setText("decoding and transforming");
        }

        @Override
        protected void onDelivered() {
            progress.setVisibility(View.INVISIBLE);
//            image.setImageLevel(0); // reset ImageView default
            text.setVisibility(View.INVISIBLE);
        }
    }
}