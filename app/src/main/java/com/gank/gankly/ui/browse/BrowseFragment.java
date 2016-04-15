package com.gank.gankly.ui.browse;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gank.gankly.R;
import com.gank.gankly.ui.base.BaseActivity;
import com.gank.gankly.ui.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BrowseFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.picture_img)
    ImageView mImageView;

    private View rootView;
    private BaseActivity mActivity;

    public BrowseFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (BaseActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_meizi, container, false);
            ButterKnife.bind(this, rootView);
        }
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
        }
    }

    @Override
    protected void initValues() {
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void bindLister() {

    }

    private void onDownRefresh() {
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (null != rootView) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static BrowseFragment newInstance(String url, int index) {
        BrowseFragment fragment = new BrowseFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onRefresh() {
        onDownRefresh();
    }
}
