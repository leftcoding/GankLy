package com.gank.gankly.ui.web;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.gank.gankly.R;
import com.gank.gankly.ui.base.BaseActivity;
import com.gank.gankly.utils.ToastUtils;
import com.gank.gankly.widget.LoveVideoView;

import butterknife.Bind;

/**
 * Create by LingYan on 2016-04-26
 */
public class WebVideoViewActivity extends BaseActivity {
    @Bind(R.id.video_view)
    LoveVideoView mLoveVideoView;
    private String mUrl;

    @Override
    protected int getContentId() {
        return R.layout.fragment_video_view;
    }

    @Override
    protected void initViews() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mUrl = bundle.getString("url");
        }
        if (!TextUtils.isEmpty(mUrl)) {
            mLoveVideoView.loadUrl(mUrl);
        } else {
            ToastUtils.showToast(R.string.tip_server_error);
        }
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void initValues() {

    }

    public static void startWebActivity(Context packageContext, Bundle bundle) {
        Intent i = new Intent(packageContext, WebVideoViewActivity.class);
        if (bundle != null) {
            i.putExtras(bundle);
        }
        packageContext.startActivity(i);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLoveVideoView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoveVideoView != null) {
            mLoveVideoView.destroy();
        }
    }
}
