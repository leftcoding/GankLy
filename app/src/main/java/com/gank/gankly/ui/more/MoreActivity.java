package com.gank.gankly.ui.more;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.gank.gankly.R;
import com.gank.gankly.ui.base.BaseActivity;
import com.gank.gankly.ui.collect.CollectFragment;

/**
 * Create by LingYan on 2016-09-21
 * Email:137387869@qq.com
 */

public class MoreActivity extends BaseActivity {
    private static final int CONTENT_ID = R.id.setting_frame_layout;
    public static final String TITLE = "title";
    public static final String TYPE = "from_type";

    public static final int TYPE_SETTING = 1;
    public static final int TYPE_COLLECT = 2;

    private int mType;

    @Override
    protected int getContentId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initValues() {
        parseIntent();

        selectFragment();
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void bindListener() {

    }

    private void parseIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mType = bundle.getInt(TYPE);
        }
    }

    private void selectFragment() {
        Fragment fragment = null;
        switch (mType) {
            case TYPE_SETTING:
                fragment = new SettingFragment();
                break;
            case TYPE_COLLECT:
                fragment = new CollectFragment();
                break;
        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .add(CONTENT_ID, fragment)
                    .commitAllowingStateLoss();
        }
    }


}
