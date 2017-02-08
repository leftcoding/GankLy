package com.gank.gankly.ui.baisi;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.ui.baisi.image.BaiSiImageFragment;
import com.gank.gankly.ui.base.BaseSwipeRefreshFragment;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.main.GankPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 首页
 * Create by LingYan on 2016-04-22
 * Email:137387869@qq.com
 */
public class BaiSiMainFragment extends BaseSwipeRefreshFragment implements ViewPager.OnPageChangeListener {
    private static final String TYPE_VIDEO = "视频";
    private static final String TYPE_IMAGE = "图片";

    @BindView(R.id.baisi_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.main_tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.main_view_pager)
    ViewPager mViewPager;

    private BaiSiActivity mActivity;
    private List<String> mTitles;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mian_baisi;
    }

    public static BaiSiMainFragment getInstance() {
        return new BaiSiMainFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaiSiActivity) context;
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    protected void initViews() {
        mToolbar.setTitle("休闲");
        mActivity.setSupportActionBar(mToolbar);
        ActionBar bar = mActivity.getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(view -> mActivity.finish());
    }

    @Override
    protected void bindListener() {
        changeThemeBackground();
    }

    @Override
    protected void initValues() {
        List<LazyFragment> mList = new ArrayList<>();
        mList.add(new BaiSiVideoFragment());
        mList.add(new BaiSiImageFragment());

        mTitles = new ArrayList<>();
        mTitles.add(TYPE_VIDEO);
        mTitles.add(TYPE_IMAGE);

        GankPagerAdapter mPagerAdapter = new GankPagerAdapter(mActivity.getSupportFragmentManager(),
                mList, mTitles);
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
        mTabLayout.setSelectedTabIndicatorColor(App.getAppColor(R.color.white));
    }

    /**
     * 改变主题颜色
     */
    private void changeThemeBackground() {
        TypedValue background = new TypedValue();//背景色
        Resources.Theme theme = mActivity.getTheme();
        theme.resolveAttribute(R.attr.themeTabLayoutBackground, background, true);
        mTabLayout.setBackgroundResource(background.resourceId);
        theme.resolveAttribute(R.attr.colorPrimary, background, true);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mActivity.setTitle(mTitles.get(position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void callBackRefreshUi() {

    }
}
