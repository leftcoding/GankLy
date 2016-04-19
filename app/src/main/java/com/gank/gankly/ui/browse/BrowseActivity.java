package com.gank.gankly.ui.browse;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.ui.base.BaseActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BrowseActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.pager)
    ViewPager mViewPager;

    private ArrayList<BrowseFragment> images;
    private PagerAdapter mPagerAdapter;
    private String mUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_picture);
        ButterKnife.bind(this);
        initValues();
        initView();
        bindLister();
    }


    private void initValues() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mUrl = bundle.getString("url");
        }

        images = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            images.add(new BrowseFragment());
        }
    }

    private void initView() {
        mToolbar.setTitle("back");
        setSupportActionBar(mToolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true); //显示返回箭头
        }

        mPagerAdapter = new PagerAdapter();
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.addOnPageChangeListener(this);
    }

    private void bindLister() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Fragment getItem(int position) {
            return BrowseFragment.newInstance(mUrl);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }
}
