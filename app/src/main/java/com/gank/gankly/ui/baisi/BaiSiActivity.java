package com.gank.gankly.ui.baisi;

import com.gank.gankly.R;
import com.gank.gankly.ui.base.BaseActivity;

/**
 * Create by LingYan on 2016-11-29
 * Email:137387869@qq.com
 */

public class BaiSiActivity extends BaseActivity {
    @Override
    protected int getContentId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initValues() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.setting_frame_layout, new BaiSiMainFragment())
                .commitAllowingStateLoss();
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void bindListener() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
