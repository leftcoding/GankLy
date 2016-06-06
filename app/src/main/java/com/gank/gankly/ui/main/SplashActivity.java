package com.gank.gankly.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.gank.gankly.R;
import com.gank.gankly.ui.base.BaseActivity;

/**
 * Create by LingYan on 2016-06-01
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_start;
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void initPresenter() {
        super.initPresenter();
    }

    @Override
    protected void initValues() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
