package com.gank.gankly.ui.browse;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.bean.GankResult;
import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.config.ViewsModel;
import com.gank.gankly.network.api.GankApi;
import com.gank.gankly.ui.base.BaseActivity;
import com.gank.gankly.ui.main.meizi.DailyMeiziFragment;
import com.gank.gankly.ui.main.meizi.GiftFragment;
import com.gank.gankly.utils.RxSaveImage;
import com.gank.gankly.utils.ShareUtils;
import com.gank.gankly.utils.ToastUtils;
import com.gank.gankly.widget.DepthPageTransformer;
import com.socks.library.KLog;

import java.io.File;
import java.util.List;

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

    @Bind(R.id.progress_txt_page)
    TextView txtLimit;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.pager)
    ViewPager mViewPager;

    private PagerAdapter mPagerAdapter;
    private int mPosition;
    private int mPage;

    private boolean isLoadMore = true;
    private String mViewsModel = ViewsModel.GANK;
    private List<GiftBean> mGiftList;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (ViewsModel.Gift.equals(mViewsModel)) {
            txtLimit.setText(App.getAppResources().getString(R.string.meizi_limit_page,
                    position + 1, mGiftList.size()));
        }else if(ViewsModel.Daily.equals(mViewsModel)){
            txtLimit.setText(App.getAppResources().getString(R.string.meizi_limit_page,
                    position + 1, mGiftList.size()));
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (ViewsModel.GANK.equals(mViewsModel)) {
            int p = MeiziArrayList.getInstance().size() - 5;
            if (position == p) {
                if (isLoadMore) {
                    fetchDate();
                }
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
        final int limit = 20;
        mPage = MeiziArrayList.getInstance().getPage();
        mPage = mPage + 1;
        GankApi.getInstance().fetchWelfare(limit, mPage, new Subscriber<GankResult>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.showToast(R.string.tip_server_error);
            }

            @Override
            public void onNext(GankResult gankResult) {
                if (!gankResult.isEmpty()) {
                    MeiziArrayList.getInstance().addBeanAndPage(gankResult.getResults(), mPage);
                }
                if (gankResult.getSize() < limit) {
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
            mPosition = bundle.getInt("position", 0);
            mViewsModel = bundle.getString(ViewsModel.Gift, ViewsModel.GANK);
        }
        if (ViewsModel.Gift.equals(mViewsModel)) {
            mGiftList = GiftFragment.getInstance().getList();
        }
        if (ViewsModel.Daily.equals(mViewsModel)) {
            mGiftList = DailyMeiziFragment.getInstance().getList();
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
            if (ViewsModel.GANK.equals(mViewsModel)) {
                return MeiziArrayList.getInstance().size();
            } else if (ViewsModel.Daily.equals(mViewsModel)) {
                return mGiftList.size();
            } else {
                return GiftFragment.getInstance().getList().size();
            }
        }

        @Override
        public Fragment getItem(int position) {
            if (ViewsModel.GANK.equals(mViewsModel)) {
                ResultsBean bean = MeiziArrayList.getInstance().getResultBean(position);
                return BrowseFragment.newInstance(bean.getUrl());
            } else if (ViewsModel.Daily.equals(mViewsModel)) {
                return BrowseFragment.newInstance(DailyMeiziFragment.getInstance()
                        .getList().get(position).getImgUrl());
            } else {
                return BrowseFragment.newInstance(GiftFragment.getInstance()
                        .getList().get(position).getImgUrl());
            }
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
                saveImagePath(getImageUrl(), false);
                break;
            case R.id.meizi_share:
                saveImagePath(getImageUrl(), true);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getImageUrl() {
        int position = mViewPager.getCurrentItem();
        String mUrl;
        if (ViewsModel.GANK.equals(mViewsModel)) {
            mUrl = MeiziArrayList.getInstance().getResultBean(position).getUrl();
        } else if (ViewsModel.Daily.equals(mViewsModel)) {
            mUrl = mGiftList.get(position).getImgUrl();
        } else {
            mUrl = mGiftList.get(position).getImgUrl();
        }
        return mUrl;
    }

    private void saveImagePath(String imgUrl, final boolean isShare) {
        RxSaveImage.saveImage(this, imgUrl)
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
                        String imgPath = getImagePath();
                        if (TextUtils.isEmpty(imgPath)) {
                            ToastUtils.showToast(R.string.tip_img_path_error);
                            return;
                        }
                        if (isShare) {
                            ShareUtils.shareSingleImage(BrowseActivity.this, uri);
                        } else {
                            String msg = String.format(getString(R.string.meizi_picture_save_path), imgPath);
                            ToastUtils.showToast(msg);
                        }
                    }
                });
    }

    private String getImagePath() {
        File appDir = new File(Environment.getExternalStorageDirectory(), "GankLy_pic");
        return appDir.getAbsolutePath();
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
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }
}
