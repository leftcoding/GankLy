package com.gank.gankly.ui.gallery;

import android.animation.Animator;
import android.app.WallpaperManager;
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
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.gank.gankly.R;
import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.mvp.source.remote.GankDataSource;
import com.gank.gankly.ui.base.activity.BaseActivity;
import com.gank.gankly.utils.CrashUtils;
import com.gank.gankly.utils.ListUtils;
import com.gank.gankly.utils.RxSaveImage;
import com.gank.gankly.utils.ShareUtils;
import com.gank.gankly.utils.StringHtml;
import com.gank.gankly.utils.ToastUtils;
import com.gank.gankly.widget.WheelView;
import com.gank.gankly.widget.transforms.FixedSpeedScroller;
import com.gank.gankly.widget.transforms.ZoomOutSlideTransformer;
import com.leftcoding.network.domain.ResultsBean;
import com.socks.library.KLog;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * 相册
 * Create by LingYan on 2016-4-25
 * Email:137387869@qq.com
 */
public class GalleryActivity extends BaseActivity implements ViewPager.OnPageChangeListener,
        GalleryContract.View {
    private static final int SYSTEM_UI_BASE_VISIBILITY = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

    private static final int SYSTEM_UI_IMMERSIVE = View.SYSTEM_UI_FLAG_IMMERSIVE
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN;

    public static final String TAG = "BrowseActivity";
    private static final String FILE_PATH = "GankLy_pic";
    public static final String EXTRA_GANK = "Gank";
    public static final String EXTRA_GIFT = "Gift";
    public static final String EXTRA_DAILY = "Daily";
    public static final String EXTRA_MODEL = "Model";
    public static final String EXTRA_POSITION = "Position";
    public static final String EXTRA_LIST = "Extra_List";

    public static final String TYPE = "Type";
    // 转场动画
    public static final int TYPE_TRANSITION = 1;
    private static final String NUMBER_COLOR = "#8b0000";

    @BindView(R.id.progress_txt_page)
    TextView txtLimit;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.pager)
    ViewPager mViewPager;
    @BindView(R.id.browse_rl)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.brose_img_auto)
    ImageView imgAuto;
    @BindView(R.id.browse_txt_auto_tip)
    TextView txtAutoTip;

    private GalleryContract.Presenter mPresenter;
    private PagerAdapter mPagerAdapter;
    private WallpaperManager myWallpaperManager;
    private Disposable subscription;
    private Disposable autoTipable;

    private List<GiftBean> mGiftList;
    private Bitmap mBitmap;
    private String mViewsModel = EXTRA_GANK;
    private boolean isCanPlay = true;
    private int mPosition;
    private int curItem = -1;
    private int transition_code;
    private int getTipWidth;

    @Override
    protected int getContentId() {
        return R.layout.activity_browse_picture;
    }

    @Override
    protected void initTheme() {
        super.initTheme();
        setTheme(R.style.BrowseThemeBase);
        mPresenter = new GalleryPresenter(GankDataSource.getInstance(), this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (EXTRA_GANK.equals(mViewsModel)) {
            boolean isFetch = isFetch();
            if (isFetch) {
                mPresenter.fetchMore();
            }
        }

        mPosition = position;
        setNumberText(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_DRAGGING) {
            hideSystemUi();
            stopBrowse();

            if (isPositionEnd()) {
                showSnackbar(mRelativeLayout, R.string.loading_all_over, ContextCompat.getColor(this, R.color.white));
            }
        }
    }

    private void setNumberText(int position) {
        int size = getAapterCount();
        String imgSize = String.valueOf(size);
        String p = String.valueOf(position + 1);
        txtLimit.setText(StringHtml.getStringSize(p, imgSize, "/", NUMBER_COLOR, 22));
    }

    @Override
    protected void initValues() {
        parseBundle();
        activityAnimation();
        getGiftList();
    }

    private void getGiftList() {
        mGiftList = new ArrayList<>();
        if (EXTRA_GANK.equals(mViewsModel)) {
            List<ResultsBean> giftBeen = MeiziArrayList.getInstance().getImagesList();
            mGiftList = changeImageList(giftBeen);
            int size = ListUtils.getSize(giftBeen);
            if (size > 5) {
                int point = size - 5;
                if (mPosition >= point) {
                    mPresenter.fetchMore();
                }
            }
        } else {
            mGiftList = getIntent().getParcelableArrayListExtra(EXTRA_LIST);
        }
    }

    private void activityAnimation() {
        Transition transition = null;
        switch (transition_code) {
            case TYPE_TRANSITION:
                transition = TransitionInflater.from(this).inflateTransition(R.transition.gallery_mid);
                break;
            default:
                break;
        }

        if (transition != null) {
            getWindow().setEnterTransition(transition);
            getWindow().setExitTransition(transition);
        }
    }

    private void parseBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPosition = bundle.getInt(EXTRA_POSITION, 0);
            mViewsModel = bundle.getString(EXTRA_MODEL, EXTRA_GANK);
            transition_code = bundle.getInt(TYPE, -1);
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

        setNumberText(mPosition);

        mViewPager.setPageTransformer(true, new ZoomOutSlideTransformer());
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setCurrentItem(mPosition);
        mViewPager.addOnPageChangeListener(this);
        mPagerAdapter.notifyDataSetChanged();

        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(mViewPager.getContext(), new LinearInterpolator());
            // scroller.setFixedDuration(5000);
            mScroller.set(mViewPager, scroller);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            KLog.e(e);
        }
    }

    @Override
    protected void bindListener() {
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private List<GiftBean> changeImageList(List<ResultsBean> resultsBeen) {
        List<GiftBean> list = new ArrayList<>();
        if (!ListUtils.isListEmpty(resultsBeen)) {
            ResultsBean resultsBean;
            String url;
            for (int i = 0; i < resultsBeen.size(); i++) {
                resultsBean = resultsBeen.get(i);
                url = resultsBean.url;
                list.add(new GiftBean(url));
            }
        }
        return list;
    }

    public void appendData(List<ResultsBean> list) {
        mGiftList.addAll(changeImageList(list));
        mPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void sysNumText() {
        setNumberText(mPosition);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void hasNoMoreDate() {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showDisNetWork() {

    }

    @Override
    public void showError() {

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

    @OnClick(R.id.brose_img_auto)
    void onBrowseAuto() {
        changeBrowse();
    }

    private void changeBrowse() {
        disposeAutoTips();

        if (isCanPlay) {
            isCanPlay = false;
            hideSystemUi();

            if (!isPositionEnd()) {
                imgAuto.setImageDrawable(getResources().getDrawable(R.drawable.ic_gallery_stop, getTheme()));
                timerBrowse();
            } else {
                stopBrowse();
            }
        } else {
            stopBrowse();
        }

        delayShowAutoTips(isCanPlay);
    }

    private void disposeAutoTips() {
        if (autoTipable != null && !autoTipable.isDisposed()) {
            autoTipable.dispose();
        }
    }

    private void delayShowAutoTips(boolean isCanPlay) {
        autoTipable = Observable.create((ObservableOnSubscribe<String>) e -> {
            e.onNext("");
            e.onComplete();
        })
                .delay(800, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (!isCanPlay) {
                        animAutoTips();
                    }
                });
    }

    private void animAutoTips() {
        txtAutoTip.animate().translationX(-getTipWidth).setDuration(800).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                txtAutoTip.setAlpha(1);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                txtAutoTip.animate().alpha(0).translationX(0).setDuration(800).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).setStartDelay(1500).start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();
    }

    private void stopBrowse() {
        isCanPlay = true;
        imgAuto.setImageDrawable(getResources().getDrawable(R.drawable.ic_gallery_play, getTheme()));
        unSubscribeTime();
    }

    @OnClick(R.id.progress_txt_page)
    void onPageClick() {
        ArrayList<String> list = getSelectList();
        if (list != null) {
            stopBrowse();
            curItem = mPosition;
            View outerView = LayoutInflater.from(this).inflate(R.layout.view_wheel, mRelativeLayout, false);
            WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
            wv.setOffset(1);
            wv.setItems(list);
            wv.setSeletion(curItem);
            wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(int selectedIndex, String item) {
                    curItem = selectedIndex - 1;
                }
            });

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(R.string.gallery_page_select);
            dialog.setView(outerView);
            dialog.setPositiveButton(R.string.dialog_ok, (dialog1, which) -> {
                mViewPager.setCurrentItem(curItem);
                dialog1.dismiss();
            });
            dialog.show();
        }
    }

    private boolean isFetch() {
        int curPosition = mViewPager.getCurrentItem() + 1;
        int p = getGiftSize() - curPosition;
        return p <= 5;
    }

    private ArrayList<String> getSelectList() {
        ArrayList<String> list = null;
        if (!isEmpty()) {
            list = new ArrayList<>();
            for (int i = 0; i < getGiftSize(); i++) {
                list.add(i, String.valueOf(i + 1));
            }
        }
        return list;
    }

    private int getGiftSize() {
        return mGiftList == null ? 0 : mGiftList.size();
    }

    private boolean isEmpty() {
        return ListUtils.isListEmpty(mGiftList);
    }

    @Override
    public void showShortToast(String string) {

    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public int getCount() {
            return getGiftSize();
        }

        @Override
        public Fragment getItem(int position) {
            return GalleryFragment.newInstance(mGiftList.get(position).getImgUrl());
        }
    }

    private int getAapterCount() {
        return mPagerAdapter == null ? 0 : mPagerAdapter.getCount();
    }

    private boolean isPositionEnd() {
        return getAapterCount() == mPosition + 1;
    }

    private void timerBrowse() {
        subscription = Observable.interval(1000, 2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (aLong >= getAapterCount()) {
                        stopBrowse();
                    } else {
                        int next = mPosition + 1;
                        mViewPager.setCurrentItem(next, true);
                    }
                    if (isPositionEnd()) {
                        stopBrowse();
                    }
                }, KLog::e);
    }

    private void unSubscribeTime() {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }

    private void makeWallpaperDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.meizi_is_wallpaper);
        builder.setNegativeButton(R.string.dialog_cancel, (dialog, which) -> dialog.cancel());
        builder.setPositiveButton(R.string.dialog_ok, (dialog, which) -> setWallPaper(getImageUrl(), GalleryActivity.this));
        builder.create().show();
    }

    private void setWallPaper(final String url, final FragmentActivity activity) {
        Observable.create((ObservableOnSubscribe<Bitmap>) subscriber -> {
            Bitmap bitmap = null;
            try {
                GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                        .addHeader("Referer", "http://www.mzitu.com/mm/")
                        .build());
                bitmap = Glide.with(activity)
                        .asBitmap()
                        .load(glideUrl)
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                .skipMemoryCache(true)
                        )
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
            } catch (InterruptedException | ExecutionException e) {
                KLog.e(e);
                CrashUtils.crashReport(e);
            }
            subscriber.onNext(bitmap);
            subscriber.onComplete();
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
                .setAction(R.string.revoke, v -> {
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
                })
                .show();
    }

    private String getImageUrl() {
        int position = mViewPager.getCurrentItem();
        String mUrl;
        if (EXTRA_GANK.equals(mViewsModel)) {
            mUrl = MeiziArrayList.getInstance().getResultBean(position).url;
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
    }

    public void switchToolbar() {
        if (mToolbar.getTranslationY() == 0) {
            hideSystemUi();
        } else {
            showSystemUi();
            stopBrowse();
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
        txtAutoTip.animate().cancel();
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        getTipWidth = txtAutoTip.getWidth();
//        KLog.d(getTipWidth);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            finishAfterTransition();
            overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
