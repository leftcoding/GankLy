package com.gank.gankly.ui.about;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.base.ToolbarActivity;

import butterknife.Bind;

public class AboutActivity extends ToolbarActivity implements View.OnClickListener{
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initValues() {

    }

    @Override
    protected void initView() {
        mToolbar.setTitle("about");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //显示返回箭头
    }

    @Override
    protected void bindLister() {
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
