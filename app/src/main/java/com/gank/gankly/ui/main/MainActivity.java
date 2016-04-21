package com.gank.gankly.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.config.Constants;
import com.gank.gankly.ui.about.AboutActivity;
import com.gank.gankly.ui.base.BaseActivity;
import com.gank.gankly.ui.collect.CollectActivity;
import com.gank.gankly.utils.AppUtils;
import com.gank.gankly.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    @Bind(R.id.main_toolbar)
    Toolbar mToolbar;

    @Bind(R.id.main_tabLayout)
    TabLayout mTabLayout;

    @Bind(R.id.main_view_pager)
    ViewPager mViewPager;

    @Bind(R.id.main_navigation)
    NavigationView mNavigationView;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    GankPagerAdapter mPagerAdapter;

    private List<LazyFragment> mList;
    private List<String> mTitles;
    private long mKeyTime;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initValues();
        initView();
        bindLister();
    }

    private void initValues() {
        mList = new ArrayList<>();
        mList.add(new WelfareFragment());
        mList.add(new WelfareFragment());
        mList.add(new MeiZiFragment());
        mList.add(new WelfareFragment());

        mTitles = new ArrayList<>();
        mTitles.add(Constants.ANDROID);
        mTitles.add(Constants.IOS);
        mTitles.add(Constants.WELFRAE);
        mTitles.add(Constants.ALL);

        mPagerAdapter = new GankPagerAdapter(getSupportFragmentManager(), mList, mTitles);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.addOnPageChangeListener(this);

        initTabLayout();
    }

    private void initTabLayout() {
        for (int i = 0; i < mTitles.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitles.get(i)));
        }
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setBackgroundColor(App.getAppColor(R.color.colorPrimary));

    }

    private void initView() {
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_home_navigation);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void bindLister() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true); // 改变item选中状态
                if (menuItem.getItemId() == R.id.navigation_collect) {
                    startActivity(new Intent(MainActivity.this, CollectActivity.class));
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setTitle(mTitles.get(position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
            if ((System.currentTimeMillis() - mKeyTime) > 2000) {
                mKeyTime = System.currentTimeMillis();
                ToastUtils.shortBottom(R.string.app_again_out);
                return false;
            } else {
                AppUtils.killProcess();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
