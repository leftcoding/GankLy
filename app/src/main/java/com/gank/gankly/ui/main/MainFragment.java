package com.gank.gankly.ui.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.bean.CheckVersion;
import com.gank.gankly.config.Constants;
import com.gank.gankly.network.DownloadProgressListener;
import com.gank.gankly.presenter.LauncherPresenter;
import com.gank.gankly.ui.base.BaseSwipeRefreshFragment;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.main.android.AndroidFragment;
import com.gank.gankly.ui.main.ios.IosFragment;
import com.gank.gankly.ui.main.welfare.WelfareFragment;
import com.gank.gankly.view.ILauncher;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 首页
 * Create by LingYan on 2016-04-22
 * Email:137387869@qq.com
 */
public class MainFragment extends BaseSwipeRefreshFragment implements ViewPager.OnPageChangeListener,
        DownloadProgressListener, ILauncher {
    @BindView(R.id.baisi_tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.main_view_pager)
    ViewPager mViewPager;

    private MainActivity mActivity;
    private List<String> mTitles;
    private LauncherPresenter mPresenter;
    private ProgressDialog mProgressDialog;
    private long mAppLength;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            changeThemeBackground();
        }
    }

    @Override
    protected void initPresenter() {
        mPresenter = new LauncherPresenter(mActivity, this, this);
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void bindListener() {
        changeThemeBackground();
    }

    @Override
    protected void initValues() {
        List<LazyFragment> mList = new ArrayList<>();
        mList.add(AndroidFragment.newInstance());
        mList.add(IosFragment.newInstance());
        mList.add(WelfareFragment.newInstance());

        mTitles = new ArrayList<>();
        mTitles.add(Constants.ANDROID);
        mTitles.add(Constants.IOS);
        mTitles.add(Constants.WELFRAE);

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
//        mToolbar.setBackgroundResource(background.resourceId);
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
    public void update(long bytesRead, long contentLength, boolean done) {
        KLog.d("bytesRead:" + bytesRead + ",contentLength:" + contentLength + ",done:" + done);
        if (bytesRead > 0 && mProgressDialog != null && mAppLength > 0) {
            mProgressDialog.setProgress((int) bytesRead);
        }

        if (done) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }

    @Override
    public void callUpdate(CheckVersion checkVersion) {
        mAppLength = checkVersion.getAppLength();
        KLog.d("mAppLength:" + mAppLength);
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage(checkVersion.getChangelog());
        builder.setNegativeButton("取消", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.setPositiveButton("更新", (dialog, which) -> {
            mPresenter.downloadApk();
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(mActivity);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(true);
                mProgressDialog.setCanceledOnTouchOutside(false);
            }
            mProgressDialog.setMessage("更新中...");
            mProgressDialog.setMax((int) mAppLength);
            mProgressDialog.show();
        });
        builder.show();
    }

    @Override
    public void showDialog() {
    }

    @Override
    public void noNewVersion() {

    }

    @Override
    public void hiddenDialog() {
    }

    @Override
    protected void callBackRefreshUi() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showDisNetWork() {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void showLoading() {

    }
}
