package com.gank.gankly.ui.browse;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gank.gankly.R;
import com.gank.gankly.ui.base.BaseFragment;
import com.gank.gankly.widget.ProgressImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BrowseFragment extends BaseFragment implements ProgressImageView.ImageViewOnClick{
    @Bind(R.id.progress_img)
    ProgressImageView mProgressImageView;
    private BrowseActivity mActivity;
    private String mUrl;

    public BrowseFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (BrowseActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_browse_picture, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseArguments();
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }


    private void parseArguments() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mUrl = bundle.getString("url");
        }
    }


    @Override
    protected void initValues() {
        mProgressImageView.load(mUrl, BrowseFragment.this);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void bindLister() {
        mProgressImageView.setImageViewOnClick(this);
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static BrowseFragment newInstance(String url) {
        BrowseFragment fragment = new BrowseFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onImageClick(View v) {
        mActivity.switchToolbar();
    }
}