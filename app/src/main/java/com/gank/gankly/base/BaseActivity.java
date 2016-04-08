package com.gank.gankly.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.socks.library.KLog;

import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-04-05
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        KLog.d("onCreate");
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initValues();
        initView();
        bindLister();
    }

    abstract protected int getLayoutId();

    abstract protected void initValues();

    abstract protected void initView();

    abstract protected void bindLister();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
