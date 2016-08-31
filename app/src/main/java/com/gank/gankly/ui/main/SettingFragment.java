package com.gank.gankly.ui.main;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.Switch;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.bean.CheckVersion;
import com.gank.gankly.config.Preferences;
import com.gank.gankly.presenter.LauncherPresenter;
import com.gank.gankly.ui.base.BaseSwipeRefreshFragment;
import com.gank.gankly.utils.AppUtils;
import com.gank.gankly.utils.GanklyPreferences;
import com.gank.gankly.utils.ToastUtils;
import com.gank.gankly.view.ILauncher;
import com.gank.gankly.widget.ItemSwitchView;
import com.gank.gankly.widget.ItemTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by LingYan on 2016-05-10
 * Email:137387869@qq.com
 */
public class SettingFragment extends BaseSwipeRefreshFragment implements ILauncher {
    @BindView(R.id.setting_rl_body)
    View mView;
    @BindView(R.id.setting_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.setting_switch_check)
    ItemSwitchView mSwitchView;
    @BindView(R.id.setting_item_text_update)
    ItemTextView itemUpdate;
    @BindView(R.id.setting_switch_theme)
    ItemSwitchView mTheme;
    @BindView(R.id.item_switch_auto_check)
    Switch mSwitch;

    public static SettingFragment sAboutFragment;
    private LauncherPresenter mPresenter;
    public MainActivity mActivity;
    private ProgressDialog mDialog;
    private Resources.Theme theme;
    private TypedValue typedValue;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new LauncherPresenter(mActivity, this);
    }

    public static SettingFragment getInstance() {
        if (sAboutFragment == null) {
            sAboutFragment = new SettingFragment();
        }
        return sAboutFragment;
    }

    @Override
    protected void initValues() {
        theme = mActivity.getTheme();
    }

    @Override
    protected void initViews() {
        mToolbar.setTitle(R.string.navigation_settings);
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

        initPreferences();
    }

    private void initPreferences() {
        boolean isAutoCheck = GanklyPreferences.getBoolean(Preferences.SETTING_AUTO_CHECK, false);
        mSwitchView.setViewSwitch(isAutoCheck);

        mTheme.setTextName(App.getAppString(R.string.setting_theme_night));

        String summary = App.getAppResources().getString(R.string.setting_current_version,
                AppUtils.getVersionName(mActivity));
        itemUpdate.setTextSummary(summary);
        itemUpdate.setTextName(R.string.setting_check_version);
        if (App.isNewVersion()) {
            itemUpdate.showVersion();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        changeTheme();
    }

    @OnClick(R.id.setting_item_text_update)
    void clikUpdate() {
        mPresenter.checkVersion();
    }

    @Override
    protected void bindLister() {
        mSwitchView.setOnSwitch(new ItemSwitchView.OnSwitch() {
            @Override
            public void onSwitch(boolean isCheck) {
                savePreferences(isCheck);
            }
        });

        if (App.isNight()) {
            mTheme.setViewSwitch(true);
        } else {
            mTheme.setViewSwitch(false);
        }

        mTheme.setOnSwitch(new ItemSwitchView.OnSwitch() {
            @Override
            public void onSwitch(boolean isCheck) {
                App.setIsNight(isCheck);
                if (isCheck) {
                    getActivity().setTheme(R.style.AppTheme_Night);
                } else {
                    getActivity().setTheme(R.style.AppTheme_Day);
                }
                refreshStatusBar();
                changeTheme();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    private void savePreferences(boolean isCheck) {
        GanklyPreferences.putBoolean(Preferences.SETTING_AUTO_CHECK, isCheck);
    }

    @Override
    public void callUpdate(CheckVersion checkVersion) {
        ToastUtils.showToast("V " + checkVersion.getVersion());
    }

    @Override
    public void noNewVersion() {
        ToastUtils.showToast(R.string.tip_no_new_version);
    }

    private void createDialog() {
        if (mDialog == null) {
            mDialog = new ProgressDialog(mActivity);
        }
        mDialog.setMessage(App.getAppString(R.string.dialog_checking));
        mDialog.show();
    }

    private void disDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    /**
     * 刷新 StatusBar
     */
    private void refreshStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            typedValue = new TypedValue();
            theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
            mActivity.getWindow().setStatusBarColor(getResources().getColor(typedValue.resourceId));
        }
    }


    private void changeTheme() {
        TypedValue background = new TypedValue();
        theme.resolveAttribute(R.attr.themeSettingBackground, background, true);
        mView.setBackgroundResource(background.resourceId);
        theme.resolveAttribute(R.attr.colorPrimary, background, true);
        mToolbar.setBackgroundResource(background.resourceId);
    }

    @Override
    public void showDialog() {
        createDialog();
    }

    @Override
    public void hiddenDialog() {
        disDialog();
    }
}
