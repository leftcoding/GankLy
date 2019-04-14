package com.gank.gankly.ui.gallery;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.ui.base.fragment.SupportFragment;
import com.gank.gankly.widget.ProgressImageView;

import butterknife.BindView;

/**
 * 图片浏览
 * Create by LingYan on 2016-12-19
 */
public class GalleryFragment extends SupportFragment implements ProgressImageView.ImageViewOnClick {
    public static final String IMAGE_URL = "Image_Url";

    @BindView(R.id.progress_img)
    ProgressImageView mProgressImageView;

    private String mUrl;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_browse_picture;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseArguments();
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressImageView.load(mUrl, GalleryFragment.this);
        mProgressImageView.setImageViewOnClick(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void parseArguments() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mUrl = bundle.getString(IMAGE_URL);
        }
    }

    public static GalleryFragment newInstance(String url) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onImageClick(View v) {
    }
}
