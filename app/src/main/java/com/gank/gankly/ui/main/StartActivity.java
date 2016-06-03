package com.gank.gankly.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.gank.gankly.R;
import com.gank.gankly.bean.CheckVersion;
import com.gank.gankly.ui.base.BaseActivity;
import com.gank.gankly.ui.presenter.LauncherPresenter;
import com.gank.gankly.ui.view.ILauncher;
import com.socks.library.KLog;

/**
 * Create by LingYan on 2016-06-01
 */
public class StartActivity extends BaseActivity<LauncherPresenter> implements ILauncher {
    private LauncherPresenter mPresenter;

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
        mPresenter = new LauncherPresenter(this, this);
        mPresenter.checkVersion();
    }

    @Override
    protected void initValues() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    @Override
    public void refillDate() {

    }

    @Override
    public void callUpdate(CheckVersion checkVersion) {
        KLog.d("checkVersion:" + checkVersion.getCode());
    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showDisNetWork() {

    }

    @Override
    public void showView() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
