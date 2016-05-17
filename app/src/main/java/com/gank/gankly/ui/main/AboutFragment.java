package com.gank.gankly.ui.main;


import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.ui.base.BaseFragment;

import butterknife.Bind;

/**
 * Create by LingYan on 2016-05-10
 */
public class AboutFragment extends BaseFragment {
    @Bind(R.id.about_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.about_collapsing)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(R.id.about_img_top)
    ImageView imgTop;

    public static AboutFragment sAboutFragment;
    public MainActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    public static AboutFragment getInstance() {
        if (sAboutFragment == null) {
            sAboutFragment = new AboutFragment();
        }
        return sAboutFragment;
    }

    @Override
    protected void initValues() {

    }

    @Override
    protected void initViews() {
        mCollapsingToolbarLayout.setTitle(App.getAppString(R.string.navigation_about));
        mActivity.setSupportActionBar(mToolbar);
        ActionBar bar = mActivity.getSupportActionBar();
        if (bar != null) {
            bar.setHomeAsUpIndicator(R.drawable.ic_home_navigation);
            bar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.openDrawer();
            }
        });
    }

    @Override
    protected void bindLister() {
        Glide.with(this)
                .load("http://7xs032.com1.z0.glb.clouddn.com/ic_launcher.png")
                .fitCenter()
                .into(imgTop);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about;
    }

}
