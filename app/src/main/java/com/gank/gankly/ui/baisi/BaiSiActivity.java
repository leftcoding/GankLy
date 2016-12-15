package com.gank.gankly.ui.baisi;

import android.support.v4.app.Fragment;

import com.gank.gankly.R;
import com.gank.gankly.ui.base.BaseActivity;
import com.socks.library.KLog;

/**
 * Create by LingYan on 2016-11-29
 * Email:137387869@qq.com
 */

public class BaiSiActivity extends BaseActivity {
    private BaiSiMainFragment mBaiSiMainFragment;

    @Override
    protected int getContentId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initValues() {
        KLog.d("initValues");
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

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack("BaiSiGalleryFragment")
                .add(R.id.setting_frame_layout, fragment)
                .commitAllowingStateLoss();
    }
}
