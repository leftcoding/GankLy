package com.gank.gankly.ui.gallery;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.ly.business.domain.Gank;
import android.ly.business.domain.Gift;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.gank.gankly.R;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.ui.base.activity.SupportActivity;
import com.gank.gankly.utils.CrashUtils;
import com.gank.gankly.utils.ListUtils;
import com.gank.gankly.utils.RxSaveImage;
import com.gank.gankly.utils.ShareUtils;
import com.gank.gankly.utils.StringHtml;
import com.gank.gankly.utils.ToastUtils;
import com.gank.gankly.widget.WheelView;
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
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * 相册
 * Create by LingYan on 2016-4-25
 */
public class GalleryActivity extends SupportActivity {
    private static final String FILE_PATH = "GankLy_pic";
    private static final int INTERVALS = 3000;
    private static final int INITIAL_DELAY = 1000;

    public static final int TYPE_INDEX = 1;

    public static final String TYPE = "Type";
    public static final String EXTRA_GANK = "Gank";
    public static final String EXTRA_GIFT = "Gift";
    public static final String EXTRA_DAILY = "Daily";
    public static final String EXTRA_MODEL = "Model";
    public static final String EXTRA_POSITION = "Position";
    public static final String EXTRA_LIST = "Extra_List";
    private static final String NUMBER_COLOR = "#8b0000";

    @BindView(R.id.progress_page)
    TextView progressPage;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.browse_auto)
    ImageView browseAuto;

    private GalleryPagerAdapter pagerAdapter;
    private WallpaperManager wallpaperManager;
    private Disposable subscription;

    private List<Gift> gifts;
    private Bitmap bitmap;
    private String browseModel = EXTRA_GANK;
    private boolean isCanPlay = true;
    private int position;

    @Override
    protected int getContentId() {
        return R.layout.activity_browse_picture;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseBundle();
        gifts = getGiftList();
        setupPageNumber(position);

        pagerAdapter = new GalleryPagerAdapter(getSupportFragmentManager());
        pagerAdapter.setGifts(gifts);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(position);
        viewPager.addOnPageChangeListener(onPageChangeListener);
        pagerAdapter.notifyDataSetChanged();
    }

    private final ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int _position) {
            position = _position;
            setupPageNumber(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                stopBrowse();

                if (isPositionEnd()) {
                    shortToast(getString(R.string.loading_all_over));
                }
            }
        }
    };

    private void setupPageNumber(int position) {
        int size = gifts == null ? 0 : gifts.size();
        String giftSize = String.valueOf(size);
        String index = String.valueOf(position + 1);
        progressPage.setText(StringHtml.getStringSize(index, giftSize, getString(R.string.page_format_mark), NUMBER_COLOR, 22));
    }

    private List<Gift> getGiftList() {
        List<Gift> gifts;
        if (EXTRA_GANK.equals(browseModel)) {
            List<Gank> giftBeen = MeiziArrayList.getInstance().getImagesList();
            gifts = changeImageList(giftBeen);
        } else {
            gifts = getIntent().getParcelableArrayListExtra(EXTRA_LIST);
        }
        return gifts == null ? new ArrayList<>() : gifts;
    }

    private void parseBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            position = bundle.getInt(EXTRA_POSITION, 0);
            browseModel = bundle.getString(EXTRA_MODEL, EXTRA_GANK);
        }
    }

    private List<Gift> changeImageList(List<Gank> resultsBeen) {
        List<Gift> list = new ArrayList<>();
        if (!ListUtils.isEmpty(resultsBeen)) {
            Gank resultsBean;
            String url;
            for (int i = 0; i < resultsBeen.size(); i++) {
                resultsBean = resultsBeen.get(i);
                url = resultsBean.url;
                list.add(new Gift(url));
            }
        }
        return list;
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

    @OnClick(R.id.browse_auto)
    void onBrowseAuto() {
        changeBrowse();
    }

    private void changeBrowse() {
        if (isCanPlay) {
            isCanPlay = false;

            if (!isPositionEnd()) {
                browseAuto.setImageDrawable(getResources().getDrawable(R.drawable.ic_gallery_stop, getTheme()));
                timerBrowse();
            } else {
                stopBrowse();
            }
        } else {
            stopBrowse();
        }
    }

    private void stopBrowse() {
        isCanPlay = true;
        browseAuto.setImageDrawable(getResources().getDrawable(R.drawable.ic_gallery_play));
        unSubscribeTime();
    }

    @OnClick(R.id.progress_page)
    void onPageClick() {
        stopBrowse();
        ArrayList<String> list = getSelectList();
        if (list != null) {
            View outerView = View.inflate(this, R.layout.view_wheel, null);
            final WheelView wv = outerView.findViewById(R.id.wheel_view_wv);
            wv.setItems(list);
            wv.setSeletion(position);
            wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(int selectedIndex, String item) {
                    position = selectedIndex - 1;
                }
            });

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(R.string.gallery_page_select);
            dialog.setView(outerView);
            dialog.setPositiveButton(R.string.dialog_ok, (dialog1, which) -> {
                dialog1.dismiss();
                viewPager.setCurrentItem(position);
            });
            dialog.show();
        }
    }

    private ArrayList<String> getSelectList() {
        ArrayList<String> list = null;
        if (!ListUtils.isEmpty(gifts)) {
            list = new ArrayList<>();
            for (int i = 0; i < getGiftSize(); i++) {
                list.add(i, String.valueOf(i + 1));
            }
        }
        return list;
    }

    private int getGiftSize() {
        return gifts == null ? 0 : gifts.size();
    }


    private int getAdapterCount() {
        return pagerAdapter == null ? 0 : pagerAdapter.getCount();
    }

    private boolean isPositionEnd() {
        return getAdapterCount() == position + 1;
    }

    private void timerBrowse() {
        subscription = Observable.interval(INITIAL_DELAY, INTERVALS, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (aLong >= getAdapterCount()) {
                        stopBrowse();
                    } else {
                        int next = position + 1;
                        viewPager.setCurrentItem(next, true);
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
                        .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
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
                        ToastUtils.showToast(getBaseContext(), R.string.meizi_wallpaper_failure);
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
                        wallpaperManager = WallpaperManager
                                .getInstance(getApplicationContext());
                        try {
                            Drawable wallpaperDrawable = wallpaperManager.getDrawable();
                            GalleryActivity.this.bitmap = ((BitmapDrawable) wallpaperDrawable).getBitmap();
                            wallpaperManager.setBitmap(bitmap);
                        } catch (IOException e) {
                            KLog.e(e);
                            CrashUtils.crashReport(e);
                        }
                    }
                });
    }

    private void revokeWallpaper() {
        Snackbar.make(viewPager, R.string.meizi_wallpaper_success, Snackbar.LENGTH_LONG)
                .setAction(R.string.revoke, v -> {
                    try {
                        if (bitmap != null) {
                            wallpaperManager.setBitmap(bitmap);
                        }
                    } catch (IOException e) {
                        KLog.e(e);
                    } finally {
                        bitmap = null;
                    }
                    ToastUtils.showToast(getBaseContext(), R.string.meizi_revoke_success);
                })
                .show();
    }

    private String getImageUrl() {
        int position = viewPager.getCurrentItem();
        String mUrl;
        if (EXTRA_GANK.equals(browseModel)) {
            mUrl = MeiziArrayList.getInstance().getResultBean(position).url;
        } else {
            mUrl = gifts.get(position).imgUrl;
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
                        ToastUtils.showToast(getBaseContext(), e.getMessage());
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
                            ToastUtils.showToast(getBaseContext(), R.string.tip_img_path_error);
                            return;
                        }
                        if (isShare) {
                            ShareUtils.shareSingleImage(GalleryActivity.this, uri);
                        } else {
                            String msg = String.format(getString(R.string.meizi_picture_save_path), imgPath);
                            ToastUtils.showToast(getBaseContext(), msg);
                        }
                    }
                });
    }

    private String getImagePath() {
        File appDir = new File(Environment.getExternalStorageDirectory(), FILE_PATH);
        return appDir.getAbsolutePath();
    }

    @Override
    protected void onDestroy() {
        unSubscribeTime();
        super.onDestroy();
    }
}
