package com.gank.gankly.ui.browse;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.gank.gankly.R;
import com.gank.gankly.bean.GankResult;
import com.gank.gankly.bean.MeiziArrayList;
import com.gank.gankly.network.GankRetrofit;
import com.gank.gankly.ui.base.BaseActivity;
import com.gank.gankly.utils.RxSaveImage;
import com.gank.gankly.utils.ToastUtils;
import com.socks.library.KLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

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
//    @Bind(R.id.appbar)
//    AppBarLayout mAppBarLayout;

    private PagerAdapter mPagerAdapter;
    private int mPosition;
    private int mPage;

    private int mLimit = 10;
    private boolean isLoadMore = true;


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
            mPosition = bundle.getInt("position");
            mPage = bundle.getInt("page");
        }

        KLog.d("mPostion:" + mPosition);
    }

    private void initView() {
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);
//        mAppBarLayout.setAlpha(0.7f);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true); //显示返回箭头
        }

        mPagerAdapter = new PagerAdapter();
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setCurrentItem(mPosition);
        mViewPager.addOnPageChangeListener(this);
        mPagerAdapter.notifyDataSetChanged();
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
        int p = MeiziArrayList.getInstance().size() - 5;
        if (position == p) {
            if (isLoadMore) {
                fetchDate(mPage);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_DRAGGING) {
            hideSystemUi();
        }
    }

    private void fetchDate(int page) {
        GankRetrofit.getInstance().fetchWelfare(mLimit, page, new Subscriber<GankResult>() {
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
            mPosition = position;
            return BrowseFragment.newInstance(MeiziArrayList.getInstance().getResultBean(position).getUrl());
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meizi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.meizi_save:
//                saveImagePath();
                saveImagePath_1();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveImagePath_1() {
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
                        KLog.d("uri" + uri);
                        File appDir = new File(Environment.getExternalStorageDirectory(), "GankLy_pic");
                        String msg = String.format(getString(R.string.meizi_picture_save_path),
                                appDir.getAbsolutePath());
                        ToastUtils.showToast(msg);
                    }
                });
    }

    private void hideSystemUi() {
        mViewPager.setSystemUiVisibility(SYSTEM_UI_BASE_VISIBILITY | SYSTEM_UI_IMMERSIVE);
        mToolbar.animate()
                .translationY(-mToolbar.getHeight())
                .setDuration(400)
//                .setInterpolator(new DecelerateInterpolator(2))
                .start();
    }

    private void showSystemUi() {
        mViewPager.setSystemUiVisibility(SYSTEM_UI_BASE_VISIBILITY);
        mToolbar.animate()
                .translationY(0)
                .setDuration(400)
//                .setInterpolator(new DecelerateInterpolator(2))
                .start();
    }

    public void switchToolbar() {
        if (mToolbar.getTranslationY() == 0) {
            hideSystemUi();
        } else {
            showSystemUi();
        }
    }


//    private void saveImagePath() {
//        ShareTask asyncTask = new ShareTask(this);
//        asyncTask.execute(mUrl);
//    }

    class ShareTask extends AsyncTask<String, Void, Bitmap> {
        private final Context context;

        public ShareTask(Context context) {
            this.context = context;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0]; // should be easy to extend to share multiple images at once
            try {
                return Glide.with(context).load(url).asBitmap()
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
            } catch (Exception ex) {
                Log.w("SHARE", "Sharing " + url + " failed", ex);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result == null) {
                return;
            }

            File appDir = new File(Environment.getExternalStorageDirectory(), "Meizhi_g");
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            String fileName = SystemClock.currentThreadTimeMillis() + ".jpg";
            File file = new File(appDir, fileName);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                assert result != null;
                result.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

}
