package com.gank.gankly.ui.main;


import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.ui.base.BaseFragment;

import butterknife.Bind;

/**
 * Create by LingYan on 2016-05-10
 */
public class AboutFragment extends BaseFragment {
    @Bind(R.id.about_toolbar)
    Toolbar mToolbar;

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
        mActivity.setSupportActionBar(mToolbar);
        ActionBar bar = mActivity.getSupportActionBar();
        if (bar != null) {
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

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about;
    }

}
