package com.gank.gankly.ui.about;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.ui.base.BaseActivity;
import com.gank.gankly.widget.RotateLoading;

import butterknife.Bind;

public class AboutActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.rotateloading)
    RotateLoading mRotateLoading;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home:
                onBackPressed();
                break;

            default:
                break;
        }
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initViews() {
        mRotateLoading.start();
        mToolbar.setTitle("about");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //显示返回箭头
    }

    @Override
    protected void bindListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void initValues() {

    }

}
