package com.gank.gankly.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-04-06
 */
public abstract class ToolbarActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    protected void initValues() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void bindLister() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
