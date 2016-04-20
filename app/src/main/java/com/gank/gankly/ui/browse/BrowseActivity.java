package com.gank.gankly.ui.browse;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.FileProvider;
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
import com.gank.gankly.ui.base.BaseActivity;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BrowseActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.pager)
    ViewPager mViewPager;

    private ArrayList<BrowseFragment> images;
    private PagerAdapter mPagerAdapter;
    private String mUrl;


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
            mUrl = bundle.getString("url");
        }

        images = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            images.add(new BrowseFragment());
        }
    }

    private void initView() {
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true); //显示返回箭头
        }

        mPagerAdapter = new PagerAdapter();
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.addOnPageChangeListener(this);
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

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Fragment getItem(int position) {
            return BrowseFragment.newInstance(mUrl);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
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
                saveImagePath();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    private void saveImagePath() {
//        RxSaveImage.saveImage(this, mUrl)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Uri>() {
//                    @Override
//                    public void onCompleted() {
//                        KLog.d("onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        KLog.d("e" + e);
//                        ToastUtils.showToast(e.getMessage() + "\n再试试...");
//                    }
//
//                    @Override
//                    public void onNext(Uri uri) {
//                        KLog.d("uri" + uri);
//                        ToastUtils.showToast("uri" + uri);
//                        File appDir = new File(Environment.getExternalStorageDirectory(), "GankLy_pic");
//                        String msg = String.format(getString(R.string.meizi_picture_save_path),
//                                appDir.getAbsolutePath());
//                        ToastUtils.showToast(msg);
//                    }
//                });
//    }

    private void saveImagePath() {
        ShareTask asyncTask = new ShareTask(this);
        asyncTask.execute(mUrl);
    }

    class ShareTask extends AsyncTask<String, Void, File> {
        private final Context context;

        public ShareTask(Context context) {
            this.context = context;
        }

        @Override
        protected File doInBackground(String... params) {
            String url = params[0]; // should be easy to extend to share multiple images at once
            try {
                return Glide
                        .with(context)
                        .load(url)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get() // needs to be called on background thread
                        ;
            } catch (Exception ex) {
                Log.w("SHARE", "Sharing " + url + " failed", ex);
                return null;
            }
        }

        @Override
        protected void onPostExecute(File result) {
            if (result == null) {
                return;
            }
            Uri uri = FileProvider.getUriForFile(context, context.getPackageName(), result);
            share(uri); // startActivity probably needs UI thread
        }

        private void share(Uri result) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpeg");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Shared image");
            intent.putExtra(Intent.EXTRA_TEXT, "Look what I found!");
            intent.putExtra(Intent.EXTRA_STREAM, result);
            context.startActivity(Intent.createChooser(intent, "Share image"));
        }
    }

    public class ImageFileProvider extends android.support.v4.content.FileProvider {
        @Override public String getType(Uri uri) { return "image/jpeg"; }
    }
}
