package com.gank.gankly.ui.baisi;

import com.gank.gankly.R;
import com.gank.gankly.ui.base.activity.BaseActivity;

/**
 * 百思不得姐
 * Create by LingYan on 2016-11-29
 */

public class BaiSiActivity extends BaseActivity {
    private BaiSiMainFragment mBaiSiMainFragment;

    @Override
    protected int getContentId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initValues() {
        if (mBaiSiMainFragment == null) {
            mBaiSiMainFragment = new BaiSiMainFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.setting_frame_layout, mBaiSiMainFragment)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void bindListener() {

    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}
