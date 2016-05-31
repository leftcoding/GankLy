package com.gank.gankly.ui.main;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.config.Constants;
import com.gank.gankly.network.service.DownloadService;
import com.gank.gankly.ui.base.BaseFragment;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.main.meizi.MeiZiFragment;
import com.gank.gankly.utils.FileUtils;
import com.google.gson.Gson;
import com.socks.library.KLog;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Create by LingYan on 2016-04-22
 */
public class MainFragment extends BaseFragment implements ViewPager.OnPageChangeListener {
    private static final String TAG = "MainFragment";

    @Bind(R.id.main_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.main_tabLayout)
    TabLayout mTabLayout;
    @Bind(R.id.main_view_pager)
    ViewPager mViewPager;

    private GankPagerAdapter mPagerAdapter;
    private MainActivity mActivity;
    private List<String> mTitles;
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
    protected void initViews() {
        mToolbar.setTitle(R.string.app_name);
        mActivity.setSupportActionBar(mToolbar);
        ActionBar ab = mActivity.getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_home_navigation);
            ab.setDisplayHomeAsUpEnabled(true);
        }
        mTabLayout.setSelectedTabIndicatorColor(App.getAppColor(R.color.white));
        getRun_1();
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

    private void getRun_1() {
        final ProgressListener progressListener = new ProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                KLog.d(bytesRead);
                KLog.d(contentLength);
                KLog.d(done);
                System.out.format("%d%% done\n", (100 * bytesRead) / contentLength);
            }
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(70, TimeUnit.SECONDS)


//                .addNetworkInterceptor(new Interceptor() {
//                    @Override
//                    public okhttp3.Response intercept(Chain chain) throws IOException {
//                        okhttp3.Response originalResponse = chain.proceed(chain.request());
//                        return originalResponse.newBuilder()
//                                .body(new ProgressResponseBody(originalResponse.body(), progressListener))
//                                .build();
//                    }
//                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("https://coding.net/u/leftcoding/p/Gank/git/raw/master/")
                .build();

        Observable<ResponseBody> call = retrofit.create(DownloadService.class).downApk();
        call.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Func1<ResponseBody, InputStream>() {
                    @Override
                    public InputStream call(ResponseBody responseBody) {
                        KLog.d("call");
                        return responseBody.byteStream();
                    }
                })
                .subscribe(new Subscriber<InputStream>() {
                    @Override
                    public void onCompleted() {
                        KLog.d("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                    }

                    @Override
                    public void onNext(InputStream response) {
                        KLog.d("response");
                        try {
                            FileUtils.writeFile(response, "gankly.apk");
                        } catch (IOException e) {
                            KLog.e(e);
                        }
                    }
                });
    }


//
//    private static class ProgressResponseBody extends ResponseBody {
//
//        private final ResponseBody responseBody;
//        private final ProgressListener progressListener;
//        private BufferedSource bufferedSource;
//
//        public ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
//            this.responseBody = responseBody;
//            this.progressListener = progressListener;
//        }
//
//        @Override
//        public MediaType contentType() {
//            return responseBody.contentType();
//        }
//
//        @Override
//        public long contentLength() {
//            return responseBody.contentLength();
//        }
//
//        @Override
//        public BufferedSource source() {
//            if (bufferedSource == null) {
//                bufferedSource = Okio.buffer(source(responseBody.source()));
//            }
//            return bufferedSource;
//        }
//
//        private Source source(Source source) {
//            return new ForwardingSource(source) {
//                long totalBytesRead = 0L;
//
//                @Override
//                public long read(Buffer sink, long byteCount) throws IOException {
//                    long bytesRead = super.read(sink, byteCount);
//                    // read() returns the number of bytes read, or -1 if this source is exhausted.
//                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
//                    progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
//                    return bytesRead;
//                }
//            };
//        }
//    }

    interface ProgressListener {
        void update(long bytesRead, long contentLength, boolean done);
    }
}
