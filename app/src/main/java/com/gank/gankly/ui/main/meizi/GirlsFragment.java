package com.gank.gankly.ui.main.meizi;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.config.Constants;
import com.gank.gankly.ui.base.BaseFragment;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 美しい妹
 * Create by LingYan on 2016-07-01
 */
public class GirlsFragment extends BaseFragment implements ViewPager.OnPageChangeListener {
    @Bind(R.id.girl_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.girl_tabLayout)
    TabLayout mTabLayout;
    @Bind(R.id.girl_view_pager)
    ViewPager mViewPager;

    private GirlsAdapter mPagerAdapter;
    private MainActivity mActivity;
    private List<String> mTitles;
    private static GirlsFragment sMainFragment;

    public static GirlsFragment getInstance() {
        if (sMainFragment == null) {
            sMainFragment = new GirlsFragment();
        }
        return sMainFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    protected void initViews() {
        mToolbar.setTitle(R.string.navigation_gift);
        mActivity.setSupportActionBar(mToolbar);
        ActionBar ab = mActivity.getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_home_navigation);
            ab.setDisplayHomeAsUpEnabled(true);
        }
        mTabLayout.setSelectedTabIndicatorColor(App.getAppColor(R.color.white));
    }

    @Override
    protected void bindLister() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.openDrawer();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_girls;
    }


    @Override
    protected void initValues() {
        List<LazyFragment> mList = new ArrayList<>();
        mList.add(GiftFragment.getInstance());
        mList.add(DailyMeiziFragment.getInstance());

        mTitles = new ArrayList<>();
        mTitles.add(Constants.QINGCHUN);
        mTitles.add(Constants.DAILY_GIRL);
        mPagerAdapter = new GirlsAdapter(mActivity.getSupportFragmentManager(), mList, mTitles);
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}