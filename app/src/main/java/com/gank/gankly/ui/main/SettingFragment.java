package com.gank.gankly.ui.main;


import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.config.Preferences;
import com.gank.gankly.ui.base.BaseFragment;
import com.gank.gankly.utils.AppUtils;
import com.gank.gankly.utils.GanklyPreferences;
import com.gank.gankly.widget.ItemSwitchView;
import com.socks.library.KLog;

import butterknife.Bind;

/**
 * Create by LingYan on 2016-05-10
 */
public class SettingFragment extends BaseFragment {
    @Bind(R.id.setting_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.setting_txt_current_version)
    TextView txtCurVersion;
    @Bind(R.id.setting_switch_check)
    ItemSwitchView mSwitchView;

    public static SettingFragment sAboutFragment;
    public MainActivity mActivity;

    @Override
    public void onAttach(Context context) {
        KLog.d("onAttach");
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

        getPreferences();
    }

    private void getPreferences() {
       boolean isAutoCheck = GanklyPreferences.getBoolean(Preferences.SETTING_AUTO_CHECK, false);
        mSwitchView.setViewSwitch(isAutoCheck);
    }

    @Override
    protected void bindLister() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    private void savePreferences() {
        GanklyPreferences.putBoolean(Preferences.SETTING_AUTO_CHECK, mSwitchView.getViewSwitch());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        KLog.d("onHiddenChanged,hidden:" + hidden);
        super.onHiddenChanged(hidden);
        if (hidden) {
            savePreferences();
        }
    }

    @Override
    public void onPause() {
        KLog.d("onPause");
        savePreferences();
        super.onPause();
    }

    @Override
    public void onStop() {
        KLog.d("onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        KLog.d("onDestroyView");
        super.onDestroyView();
    }
}
