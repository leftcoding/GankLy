package com.gank.gankly.ui.main;


import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.ui.base.BaseFragment;
import com.gank.gankly.utils.AppUtils;

import butterknife.Bind;

/**
 * Create by LingYan on 2016-05-10
 */
public class SettingFragment extends BaseFragment {
    @Bind(R.id.setting_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.setting_txt_current_version)
    TextView txtCurVersion;

    public static SettingFragment sAboutFragment;
    public MainActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    public static SettingFragment getInstance() {
        if (sAboutFragment == null) {
            sAboutFragment = new SettingFragment();
        }
        return sAboutFragment;
    }

    @Override
    protected void initValues() {
    }

    @Override
    protected void initViews() {
        mToolbar.setTitle(R.string.navigation_settings);
        txtCurVersion.setText(App.getAppResources().getString(R.string.setting_current_version,
                AppUtils.getVersionName(mActivity)));
        mActivity.setSupportActionBar(mToolbar);
        ActionBar bar = mActivity.getSupportActionBar();
        if (bar != null) {
            bar.setHomeAsUpIndicator(R.drawable.ic_home_navigation);
            bar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.openDrawer();
            }
        });
    }

    @Override
    protected void bindLister() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

}
