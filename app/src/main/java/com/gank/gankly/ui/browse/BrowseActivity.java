package com.gank.gankly.ui.browse;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.gank.gankly.R;
import com.gank.gankly.bean.GankResult;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.network.GankRetrofit;
import com.gank.gankly.ui.base.BaseActivity;
import com.gank.gankly.utils.RxSaveImage;
import com.gank.gankly.utils.ToastUtils;
import com.gank.gankly.widget.DepthPageTransformer;
import com.socks.library.KLog;

import java.io.File;

import butterknife.Bind;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Create by LingYan on 2016-4-25
 */
public class BrowseActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private static final int SYSTEM_UI_BASE_VISIBILITY = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

    private static final int SYSTEM_UI_IMMERSIVE = View.SYSTEM_UI_FLAG_IMMERSIVE
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.pager)
    ViewPager mViewPager;

    private PagerAdapter mPagerAdapter;
    private int mPosition;
    private int mPage;

    private int mLimit = 10;
    private boolean isLoadMore = true;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int p = MeiziArrayList.getInstance().size() - 5;
        if (position == p) {
            if (isLoadMore) {
                fetchDate();
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_DRAGGING) {
            hideSystemUi();
        }
    }

    private void fetchDate() {
        GankRetrofit.getInstance().fetchWelfare(mLimit, mPage, new Subscriber<GankResult>() {
            @Override
            public void onCompleted() {
                mPage = mPage + 1;
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(GankResult gankResult) {
                if (!gankResult.isEmpty()) {
                    if (mPage == 1) {
                        MeiziArrayList.getInstance().clear();
                    }
                    MeiziArrayList.getInstance().addAll(gankResult.getResults());
                }
                if (gankResult.getSize() < mLimit) {
                    isLoadMore = false;
                    ToastUtils.longBottom(R.string.loading_pic_no_more);
                }
                mPagerAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_browse_picture;
    }

    @Override
    protected void initValues() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPosition = bundle.getInt("position");
            mPage = bundle.getInt("page");
        }
    }

    @Override
    protected void initViews() {
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true); //显示返回箭头
        }

        mPagerAdapter = new PagerAdapter();
        mViewPager.setAdapter(mPagerAdapter);
//        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setCurrentItem(mPosition);
        mViewPager.addOnPageChangeListener(this);
        mPagerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void bindListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public int getCount() {
            return MeiziArrayList.getInstance().size();
        }

        @Override
        public Fragment getItem(int position) {
            ResultsBean bean = MeiziArrayList.getInstance().getResultBean(position);
            return BrowseFragment.newInstance(bean.getUrl());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meizi_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.meizi_save:
                saveImagePath();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveImagePath() {
        String mUrl = MeiziArrayList.getInstance().getResultBean(mPosition).getUrl();
        RxSaveImage.saveImage(this, mUrl)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Uri>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e("e" + e);
                        ToastUtils.showToast(e.getMessage() + "\n再试试...");
                    }

                    @Override
                    public void onNext(Uri uri) {
                        File appDir = new File(Environment.getExternalStorageDirectory(), "GankLy_pic");
                        String msg = String.format(getString(R.string.meizi_picture_save_path), appDir.getAbsolutePath());
                        ToastUtils.showToast(msg);
                    }
                });
    }

    private void hideSystemUi() {
        mViewPager.setSystemUiVisibility(SYSTEM_UI_BASE_VISIBILITY | SYSTEM_UI_IMMERSIVE);
        mToolbar.animate()
                .translationY(-mToolbar.getHeight())
                .setDuration(400)
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
    }

    private void showSystemUi() {
        mViewPager.setSystemUiVisibility(SYSTEM_UI_BASE_VISIBILITY);
        mToolbar.animate()
                .translationY(0)
                .setDuration(400)
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
    }

    public void switchToolbar() {
        if (mToolbar.getTranslationY() == 0) {
            hideSystemUi();
        } else {
            showSystemUi();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition( R.anim.alpha_in,R.anim.alpha_out);
    }
}
