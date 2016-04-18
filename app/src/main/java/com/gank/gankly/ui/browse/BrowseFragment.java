package com.gank.gankly.ui.browse;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gank.gankly.R;
import com.gank.gankly.ui.base.BaseActivity;
import com.gank.gankly.ui.base.BaseFragment;
import com.socks.library.KLog;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BrowseFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.browse_img)
    ImageView mImageView;


    private BaseActivity mActivity;
    private String mUrl;
    private String mIndex;
    private String diskCache = Environment.getExternalStorageDirectory().getAbsolutePath() + "/GankLy/cache/img";

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
    }

    @Override
    protected void initViews() {
        KLog.d("mUrl:" + mUrl);
        int last = mUrl.lastIndexOf("/");
        String sub = mUrl.substring(last + 1, mUrl.length());
        KLog.d("last:" + last + "sub:" + sub);
        sub = diskCache + "/" + sub;
        KLog.d("sub:" + sub);
        File file = new File(sub);
//        if (file.isDirectory()) {
        Glide.with(this).load(mUrl).into(mImageView);
//        } else {
//            KLog.d("file not directory");
//        }
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
    public void onRefresh() {
        onDownRefresh();
    }
}
