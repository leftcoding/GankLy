package com.gank.gankly.ui.gallery;

import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.ui.base.BaseActivity;
import com.gank.gankly.utils.CrashUtils;
import com.gank.gankly.utils.ListUtils;
import com.gank.gankly.utils.RxSaveImage;
import com.gank.gankly.utils.ShareUtils;
import com.gank.gankly.utils.ToastUtils;
import com.gank.gankly.widget.DepthPageTransformer;
import com.socks.library.KLog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Create by LingYan on 2016-4-25
 */
public class GalleryActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private static final int SYSTEM_UI_BASE_VISIBILITY = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

    private static final int SYSTEM_UI_IMMERSIVE = View.SYSTEM_UI_FLAG_IMMERSIVE
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN;


    private static final String FILE_PATH = "GankLy_pic";
    public static final String TAG = "BrowseActivity";
    public static final String EXTRA_GANK = "Gank";
    public static final String EXTRA_GIFT = "Gift";
    public static final String EXTRA_DAILY = "Daily";
    public static final String EXTRA_MODEL = "Model";
    public static final String EXTRA_POSITION = "Position";
    public static final String EXTRA_LIST = "Extra_List";

    @BindView(R.id.progress_txt_page)
    TextView txtLimit;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.pager)
    ViewPager mViewPager;
    @BindView(R.id.brose_img_auto)
    ImageView mImageView;
    @BindView(R.id.browse_rl)
    RelativeLayout mRelativeLayout;


    private PagerAdapter mPagerAdapter;
    private int mPosition;
    private int mPage;

    private boolean isLoadMore = true;
    private String mViewsModel = EXTRA_GANK;
    private List<GiftBean> mGiftList = new ArrayList<>();
    private Bitmap mBitmap;
    private WallpaperManager myWallpaperManager;
    private Disposable subscription;
    private boolean isAutoScroll = false;
    private boolean isPlay;

    @Override
    protected int getContentId() {
        return R.layout.activity_browse_picture;
    }

    @Override
    protected void initTheme() {
        super.initTheme();
        setTheme(R.style.BrowseThemeBase);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (EXTRA_GANK.equals(mViewsModel)) {
            int p = mGiftList.size() - 5;
            if (position == p) {
                if (isLoadMore) {
                    fetchDate();
                }
            }
        }

        if (!isAutoScroll) {
            mPosition = position;
        }
        setNumberText(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_DRAGGING) {
            isAutoScroll = false;
            hideSystemUi();
            unSubscribeTime();
        }
    }

    private void fetchDate() {
        final int limit = 20;
        mPage = MeiziArrayList.getInstance().getPage();
        mPage = mPage + 1;
//        GankApi.getInstance().fetchWelfare(limit, mPage, new Observable<GankResult>() {
//
//
//            @Override
//            public void onCompleted() {
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                KLog.e(e);
//                CrashUtils.crashReport(e);
//                ToastUtils.showToast(R.string.tip_server_error);
//            }
//
//            @Override
//            public void onNext(GankResult gankResult) {
//                if (!gankResult.isEmpty()) {
//                    MeiziArrayList.getInstance().addBeanAndPage(gankResult.getResults(), mPage);
//                    mGiftList.addAll(changeImageList(gankResult.getResults()));
//                }
//                if (gankResult.getSize() < limit) {
//                    isLoadMore = false;
//                    ToastUtils.longBottom(R.string.loading_pic_no_more);
//                }
//                mPagerAdapter.notifyDataSetChanged();
//            }
//        });
    }

    private void setNumberText(int position) {
        int size = getAapterCount();
        txtLimit.setText(App.getAppResources().getString(R.string.meizi_limit_page,
                position + 1, size));
    }

    @Override
    protected void initValues() {
        Bundle bundle = getIntent().getBundleExtra(TAG);
        if (bundle != null) {
            mPosition = bundle.getInt(EXTRA_POSITION, 0);
            mViewsModel = bundle.getString(EXTRA_MODEL, EXTRA_GANK);
        }

        if (EXTRA_GANK.equals(mViewsModel)) {
            List<ResultsBean> giftBeen = MeiziArrayList.getInstance().getOneItemsList();
            mGiftList = changeImageList(giftBeen);
        } else {
            mGiftList = (ArrayList<GiftBean>) getIntent().getSerializableExtra(EXTRA_LIST);
        }
    }

    @Override
    protected void initViews() {
        mPagerAdapter = new PagerAdapter();
        mViewPager.setAdapter(mPagerAdapter);

        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true); //显示返回箭头
        }

        mImageView.setBackgroundResource(R.drawable.ic_gallery_play);

        setNumberText(mPosition);

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

    private List<GiftBean> changeImageList(List<ResultsBean> giftBeen) {
        List<GiftBean> list = new ArrayList<>();
        if (!ListUtils.isListEmpty(giftBeen)) {
            for (ResultsBean gift : giftBeen) {
                list.add(new GiftBean(gift.getUrl()));
            }
        }
        return list;
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public int getCount() {
            return mGiftList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return GalleryFragment.newInstance(mGiftList.get(position).getImgUrl());
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
            case R.id.meizi_wallpaper:
                makeWallpaperDialog();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private int getAapterCount() {
        return mPagerAdapter == null ? 0 : mPagerAdapter.getCount();
    }

    private boolean isPositionEnd() {
        return getAapterCount() == mPosition + 1;
    }

    private void timerBrowse() {
        if (!isPositionEnd()) {
            mImageView.setBackgroundResource(R.drawable.ic_gallery_stop);
            isAutoScroll = true;
            subscription = Observable.interval(1000, 2000, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            if (aLong >= getAapterCount()) {
                                unSubscribeTime();
                            } else {
                                mPosition = mPosition + 1;
                                mViewPager.setCurrentItem(mPosition);
                            }

                            if (isPositionEnd()) {
                                unSubscribeTime();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {

                        }
                    });
        }
    }

    private void unSubscribeTime() {
        mImageView.setBackgroundResource(R.drawable.ic_gallery_play);
        if (subscription != null) {
            subscription.dispose();
        }
    }

    private void makeWallpaperDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.meizi_is_wallpaper);
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setWallPaper(getImageUrl(), GalleryActivity.this);
            }
        });
        builder.create().show();
    }

    private void setWallPaper(final String url, final FragmentActivity activity) {
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> subscriber) throws Exception {
                Bitmap bitmap = null;
                try {
                    bitmap = Glide.with(activity)
                            .load(url)
                            .asBitmap()
                            .atMost()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .skipMemoryCache(true)
                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                } catch (InterruptedException | ExecutionException e) {
                    KLog.e(e);
                    CrashUtils.crashReport(e);
                }
                subscriber.onNext(bitmap);
                subscriber.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showToast(R.string.meizi_wallpaper_failure);
                        KLog.e(e);
                        CrashUtils.crashReport(e);
                    }

                    @Override
                    public void onComplete() {
                        revokeWallpaper();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        myWallpaperManager = WallpaperManager
                                .getInstance(getApplicationContext());
                        try {
                            Drawable wallpaperDrawable = myWallpaperManager.getDrawable();
                            mBitmap = ((BitmapDrawable) wallpaperDrawable).getBitmap();
                            myWallpaperManager.setBitmap(bitmap);
                        } catch (IOException e) {
                            KLog.e(e);
                            CrashUtils.crashReport(e);
                        }
                    }
                });
    }

    private void revokeWallpaper() {
        Snackbar.make(mViewPager, R.string.meizi_wallpaper_success, Snackbar.LENGTH_LONG)
                .setAction(R.string.revoke, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (mBitmap != null) {
                                myWallpaperManager.setBitmap(mBitmap);
                            }
                        } catch (IOException e) {
                            KLog.e(e);
                        } finally {
                            mBitmap = null;
                        }
                        ToastUtils.showToast(R.string.meizi_revoke_success);
                    }
                })
                .show();
    }

    private String getImageUrl() {
        int position = mViewPager.getCurrentItem();
        String mUrl;
        if (EXTRA_GANK.equals(mViewsModel)) {
            mUrl = MeiziArrayList.getInstance().getResultBean(position).getUrl();
        } else {
            mUrl = mGiftList.get(position).getImgUrl();
        }
        return mUrl;
    }

    private void saveImagePath(String imgUrl, final boolean isShare) {
        RxSaveImage.saveImageAndGetPathObservable(this, imgUrl)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Uri>() {
                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                        CrashUtils.crashReport(e);
                        ToastUtils.showToast(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Uri uri) {
                        String imgPath = getImagePath();
                        if (TextUtils.isEmpty(imgPath)) {
                            ToastUtils.showToast(R.string.tip_img_path_error);
                            return;
                        }
                        if (isShare) {
                            ShareUtils.shareSingleImage(GalleryActivity.this, uri);
                        } else {
                            String msg = String.format(getString(R.string.meizi_picture_save_path), imgPath);
                            ToastUtils.showToast(msg);
                        }
                    }
                });
    }

    @OnClick(R.id.brose_img_auto)
    void onBrowseAuto() {
        if (isPlay) {
            unSubscribeTime();
        } else {
            hideSystemUi();
            timerBrowse();
        }
        isPlay = !isPlay;
    }

    private String getImagePath() {
        File appDir = new File(Environment.getExternalStorageDirectory(), FILE_PATH);
        return appDir.getAbsolutePath();
    }

    private void hideSystemUi() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        mToolbar.animate()
                .translationY(-mToolbar.getHeight())
                .setDuration(400)
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
    }

    private void showSystemUi() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        mToolbar.animate()
                .translationY(0)
                .setDuration(400)
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
        unSubscribeTime();
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

    @Override
    protected void onDestroy() {
        unSubscribeTime();
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
