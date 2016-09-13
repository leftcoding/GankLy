package com.gank.gankly.ui.main;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.RxBus.ChangeThemeEvent.ThemeEvent;
import com.gank.gankly.RxBus.RxBus;
import com.gank.gankly.bean.CheckVersion;
import com.gank.gankly.config.Preferences;
import com.gank.gankly.listener.DialogOnClick;
import com.gank.gankly.presenter.LauncherPresenter;
import com.gank.gankly.ui.base.BaseSwipeRefreshFragment;
import com.gank.gankly.utils.AppUtils;
import com.gank.gankly.utils.GanklyPreferences;
import com.gank.gankly.utils.ToastUtils;
import com.gank.gankly.view.ILauncher;
import com.gank.gankly.widget.ItemSwitchView;
import com.gank.gankly.widget.ItemTextView;
import com.gank.gankly.widget.UpdateVersionDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置
 * Create by LingYan on 2016-05-10
 * Email:137387869@qq.com
 */
public class SettingFragment extends BaseSwipeRefreshFragment implements ILauncher {
    public static final String IS_NIGHT = "isNight";
    private static final String DIALOG_TAG = "versionDialog";

    @BindView(R.id.setting_rl_body)
    View mView;
    @BindView(R.id.setting_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.setting_switch_check)
    ItemSwitchView mAutoCheckSwitch;
    @BindView(R.id.setting_item_text_update)
    ItemTextView itemUpdate;
    @BindView(R.id.setting_switch_theme)
    ItemSwitchView mThemeSwitch;
    @BindViews({R.id.setting_switch_check, R.id.setting_switch_theme})
    List<ItemSwitchView> switchTextViews;
    @BindViews({R.id.setting_item_text_update})
    List<ItemTextView> textViews;
    @BindView(R.id.setting_text_copyright)
    TextView txtCopyRight;

    public static SettingFragment sAboutFragment;
    private LauncherPresenter mPresenter;
    public MainActivity mActivity;
    private ProgressDialog mProgressDialog;
    private Resources.Theme theme;
    private TypedValue textColor;
    private UpdateVersionDialog mVersionDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    public static SettingFragment getInstance() {
        if (sAboutFragment == null) {
            sAboutFragment = new SettingFragment();
        }
        return sAboutFragment;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new LauncherPresenter(mActivity, this);
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
        mThemeSwitch.setTextName(App.getAppString(R.string.setting_theme_night));

        String summary = App.getAppResources().getString(R.string.setting_current_version,
                AppUtils.getVersionName(mActivity));
        itemUpdate.setTextSummary(summary);
        itemUpdate.setTextName(R.string.setting_check_version);

        if (App.isNewVersion()) {
            itemUpdate.showVersion();
        }

        selectItemSwitch();
    }

    private void selectItemSwitch() {
        boolean isAutoCheck = GanklyPreferences.getBoolean(Preferences.SETTING_AUTO_CHECK, true);
        mAutoCheckSwitch.setSwitchChecked(isAutoCheck);
        boolean isNight = App.isNight();
        mThemeSwitch.setSwitchChecked(isNight);
    }

    @Override
    protected void bindLister() {
        mAutoCheckSwitch.setSwitchListener(new ItemSwitchView.OnSwitch() {
            @Override
            public void onSwitch(boolean isCheck) {
                savePreferences(isCheck);
            }
        });

        mThemeSwitch.setSwitchListener(new ItemSwitchView.OnSwitch() {
            @Override
            public void onSwitch(boolean isCheck) {
                App.setIsNight(isCheck);
                GanklyPreferences.putBoolean(IS_NIGHT, isCheck);

                if (isCheck) {
                    mActivity.setTheme(R.style.AppTheme_Night);
                } else {
                    mActivity.setTheme(R.style.AppTheme_Day);
                }

                RxBus.getInstance().post(new ThemeEvent(isCheck));
                refreshStatusBar();
                changeUi();
            }
        });
    }

    private void savePreferences(boolean isCheck) {
        GanklyPreferences.putBoolean(Preferences.SETTING_AUTO_CHECK, isCheck);
    }

    @Override
    public void callUpdate(CheckVersion checkVersion) {
        showVersionDialog(checkVersion.getChangelog());
    }

    @Override
    public void noNewVersion() {
        ToastUtils.showToast(R.string.tip_no_new_version);
    }

    private void showVersionDialog(String content) {
        Bundle bundle = new Bundle();
        bundle.putString(UpdateVersionDialog.UPDATE_CONTENT, content);

        if (mVersionDialog == null) {
            mVersionDialog = new UpdateVersionDialog();
        }
        mVersionDialog.setDialogOnClick(new DialogOnClick() {
            @Override
            public void cancel() {
                mVersionDialog.dismiss();
            }

            @Override
            public void submit() {
                mVersionDialog.dismiss();
                ToastUtils.showToast(R.string.update_downing);
                mPresenter.downloadApk();
            }
        });
        mVersionDialog.setArguments(bundle);
        mVersionDialog.show(mActivity.getSupportFragmentManager(), DIALOG_TAG);
    }

    private void createDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mActivity);
        }
        mProgressDialog.setMessage(App.getAppString(R.string.dialog_checking));
        mProgressDialog.show();
    }

    private void disDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * 刷新 StatusBar
     */
    private void refreshStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            TypedValue typedValue = new TypedValue();
            theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
            mActivity.getWindow().setStatusBarColor(getResources().getColor(typedValue.resourceId));
        }
    }

    private void changeUi() {
        TypedValue background = new TypedValue();
        theme.resolveAttribute(R.attr.settingBackground, background, true);
        mView.setBackgroundResource(background.resourceId);
        theme.resolveAttribute(R.attr.colorPrimary, background, true);
        mToolbar.setBackgroundResource(background.resourceId);
        textColor = new TypedValue();
        theme.resolveAttribute(R.attr.TextTitleColor, textColor, true);
        int copyRight = textColor.data;
        txtCopyRight.setTextColor(copyRight);
        theme.resolveAttribute(R.attr.textPrimaryColor, textColor, true);

        ButterKnife.apply(switchTextViews, new ButterKnife.Action<ItemSwitchView>() {
            @Override
            public void apply(@NonNull ItemSwitchView view, int index) {
                view.getTextView().setTextColor(App.getAppColor(textColor.resourceId));
                view.getSwitch().changeTheme();
            }
        });

        ButterKnife.apply(textViews, new ButterKnife.Action<ItemTextView>() {
            @Override
            public void apply(@NonNull ItemTextView view, int index) {
                view.getTextView().setTextColor(App.getAppColor(textColor.resourceId));
            }
        });
    }

    @Override
    public void showDialog() {
        createDialog();
    }

    @Override
    public void hiddenDialog() {
        disDialog();
    }

    @OnClick(R.id.setting_item_text_update)
    void clikUpdate() {
        mPresenter.checkVersion();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
