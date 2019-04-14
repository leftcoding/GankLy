package com.gank.gankly.ui.baisi;

import android.os.Bundle;
import androidx.annotation.Nullable;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mBaiSiMainFragment == null) {
            mBaiSiMainFragment = new BaiSiMainFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.setting_frame_layout, mBaiSiMainFragment)
                    .commitAllowingStateLoss();
        }
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
