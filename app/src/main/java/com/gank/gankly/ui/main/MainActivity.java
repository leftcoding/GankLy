package com.gank.gankly.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.base.BaseActivity;
import com.gank.gankly.bean.GankResult;
import com.gank.gankly.config.Constants;
import com.gank.gankly.network.GankRetrofit;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;

public class MainActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.tabLayout)
    TabLayout mTabLayout;

    @Bind(R.id.home_view_pager)
    ViewPager mViewPager;

    GankPagerAdapter mPagerAdapter;
    List<Fragment> mList;

    List<String> mTitles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        KLog.d("onCreate");
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initValues() {
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
    }

    @Override
    protected void initView() {
        mToolbar.setTitle("首页");
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void bindLister() {

    }

    private void fetchDate() {
        KLog.d("fetchDate");
        GankRetrofit.getInstance().fetchAndroid(10, 1, new Subscriber<GankResult>() {
            @Override
            public void onCompleted() {
                KLog.d("fetchAndroid fetchDate onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                KLog.e("fetchAndroid fetchDate onError" + e);
            }

            @Override
            public void onNext(GankResult gankResult) {
                KLog.d(gankResult);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home:

                break;

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
