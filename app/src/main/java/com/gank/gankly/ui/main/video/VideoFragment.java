package com.gank.gankly.ui.main.video;

import android.os.Handler;

import com.gank.gankly.R;
import com.gank.gankly.bean.GankResult;
import com.gank.gankly.network.GankRetrofit;
import com.gank.gankly.ui.base.BaseFragment;
import com.socks.library.KLog;

import rx.Subscriber;

/**
 * Create by LingYan on 2016-04-25
 */
public class VideoFragment extends BaseFragment {
    private int mLimit = 20;
    private int mPage;
    private static VideoFragment sVideoFragment;

    public static VideoFragment getIntance() {
        if (sVideoFragment == null) {
            sVideoFragment = new VideoFragment();
        }
        return sVideoFragment;
    }

    @Override
    protected void initValues() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onDownRefresh();
            }
        }, 500);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void bindLister() {

    }

    private void onDownRefresh() {
        mPage = 1;
        fetchVideo();
    }

    private void fetchVideo() {
        Subscriber<GankResult> subscriber = new Subscriber<GankResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(GankResult gankResult) {
                KLog.d("gankResult:" + gankResult.getSize());
            }
        };

        GankRetrofit.getInstance().fetchVideo(mLimit, mPage, subscriber);
    }


    @Override

    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

}
