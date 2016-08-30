package com.gank.gankly.ui.main;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.bean.CheckVersion;
import com.gank.gankly.config.Constants;
import com.gank.gankly.config.Preferences;
import com.gank.gankly.network.DownloadProgressListener;
import com.gank.gankly.presenter.LauncherPresenter;
import com.gank.gankly.ui.base.BaseSwipeRefreshFragment;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.main.meizi.MeiZiFragment;
import com.gank.gankly.utils.GanklyPreferences;
import com.gank.gankly.view.ILauncher;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Create by LingYan on 2016-04-22
 * Email:137387869@qq.com
 */
public class MainFragment extends BaseSwipeRefreshFragment implements
        ViewPager.OnPageChangeListener, DownloadProgressListener, ILauncher {
    private static final String TAG = "MainFragment";

    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.main_tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.main_view_pager)
    ViewPager mViewPager;

    private GankPagerAdapter mPagerAdapter;
    private MainActivity mActivity;
    private List<String> mTitles;
    private LauncherPresenter mPresenter;
    private static MainFragment sMainFragment;

    public static MainFragment getInstance() {
        if (sMainFragment == null) {
            sMainFragment = new MainFragment();
        }
        return sMainFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new LauncherPresenter(mActivity, this, this);
        boolean isAutoCheck = GanklyPreferences.getBoolean(Preferences.SETTING_AUTO_CHECK, true);
        if (isAutoCheck) {
            mPresenter.checkVersion();
        }
    }

    @Override
    protected void initViews() {
        mToolbar.setTitle(R.string.app_name);
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
        return R.layout.include_app_head;
    }


    @Override
    protected void initValues() {
        List<LazyFragment> mList = new ArrayList<>();
        mList.add(new AndroidFragment());
        mList.add(new IosFragment());
        mList.add(new MeiZiFragment());

        mTitles = new ArrayList<>();
        mTitles.add(Constants.ANDROID);
        mTitles.add(Constants.IOS);
        mTitles.add(Constants.WELFRAE);
        mPagerAdapter = new GankPagerAdapter(mActivity.getSupportFragmentManager(), mList, mTitles);
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
        mActivity.setTitle(mTitles.get(position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void downloadApk() {
        mPresenter.downloadApk();
    }

    @Override
    public void update(long bytesRead, long contentLength, boolean done) {
        KLog.d("bytesRead:" + bytesRead + ",contentLength:" + contentLength + ",done:" + done);
    }

    @Override
    public void callUpdate(CheckVersion checkVersion) {
        downloadApk();
    }

    @Override
    public void noNewVersion() {
    }

    @Override
    public void showDialog() {

    }

    @Override
    public void hiddenDialog() {

    }
}
