package com.gank.gankly.ui.about;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.ui.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        initValues();
        initView();
        bindLister();
    }


    private void initValues() {

    }

    private void initView() {
        mToolbar.setTitle("about");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //显示返回箭头
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home:
                onBackPressed();
                break;

            default:
                break;
        }
    }
}
