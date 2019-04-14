package com.gank.gankly.ui.more;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.gank.gankly.R;
import com.gank.gankly.bean.CheckVersion;
import com.gank.gankly.config.Preferences;
import com.gank.gankly.listener.DialogOnClick;
import com.gank.gankly.ui.base.fragment.SupportFragment;
import com.gank.gankly.ui.main.LauncherPresenter;
import com.gank.gankly.utils.AppUtils;
import com.gank.gankly.utils.GanklyPreferences;
import com.gank.gankly.utils.GlideCatchUtil;
import com.gank.gankly.utils.ToastUtils;
import com.gank.gankly.view.ILauncher;
import com.gank.gankly.widget.ItemSwitchView;
import com.gank.gankly.widget.ItemTextView;
import com.gank.gankly.widget.UpdateVersionDialog;
import com.tencent.bugly.beta.Beta;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

/**
 * 设置
 * Create by LingYan on 2016-05-10
 */
public class SettingFragment extends SupportFragment implements ILauncher {
    public static final String TAG = "SettingFragment";

    public static final String IS_NIGHT = "isNight";
    private static final String DIALOG_TAG = "versionDialog";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.setting_rl_body)
    View mView;
    @BindView(R.id.setting_switch_check)
    ItemSwitchView itemCheckSwitch;
    @BindView(R.id.setting_item_text_clean_cache)
    ItemTextView itemCleanCache;
    @BindView(R.id.setting_item_text_update)
    ItemTextView itemUpdate;
    @BindView(R.id.setting_text_copyright)
    TextView txtCopyRight;
    @BindView(R.id.setting_switch_only_wifi)
    ItemSwitchView itemOnlyWifi;
    @BindViews({R.id.setting_switch_check})
    List<ItemSwitchView> switchTextViews;
    @BindViews({R.id.setting_item_text_update})
    List<ItemTextView> textViews;

    private LauncherPresenter mPresenter;
    public MoreActivity mActivity;
    private ProgressDialog mProgressDialog;
    private UpdateVersionDialog mVersionDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    public static SettingFragment getInstance() {
        return new SettingFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPreferences();

        mToolbar.setTitle(R.string.navigation_settings);
        mActivity.setSupportActionBar(mToolbar);
        ActionBar bar = mActivity.getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(v -> mActivity.onBackPressed());

        String cacheSize = GlideCatchUtil.getInstance().getCacheSize(getContext());
        itemCleanCache.setTextSummary(mActivity.getResources().getString(R.string.setting_picture_cache, cacheSize));

        itemCheckSwitch.setSwitchListener(isCheck -> GanklyPreferences.putBoolean(getContext(), Preferences.SETTING_AUTO_CHECK, isCheck));
        itemOnlyWifi.setSwitchListener(isCheck -> GanklyPreferences.putBoolean(getContext(), Preferences.SETTING_WIFI_ONLY, isCheck));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new LauncherPresenter(mActivity, this);
    }


    private void initPreferences() {
        String summary = getContext().getString(R.string.setting_current_version,
                AppUtils.getVersionName(mActivity));
        itemUpdate.setTextSummary(summary);
        itemUpdate.setTextName(R.string.setting_check_version);

        selectItemSwitch();
    }

    private void selectItemSwitch() {
        boolean isAutoCheck = GanklyPreferences.getBoolean(getContext(), Preferences.SETTING_AUTO_CHECK, true);
        itemCheckSwitch.setSwitchChecked(isAutoCheck);
        boolean isOnlyWifi = GanklyPreferences.getBoolean(getContext(), Preferences.SETTING_WIFI_ONLY, false);
        itemOnlyWifi.setSwitchChecked(isOnlyWifi);
    }

    @Override
    public void callUpdate(CheckVersion checkVersion) {
        showVersionDialog(checkVersion.getChangelog());
    }

    @Override
    public void noNewVersion() {
        ToastUtils.showToast(getContext(), R.string.tip_no_new_version);
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
                ToastUtils.showToast(getContext(), R.string.update_downing);
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
        mProgressDialog.setMessage(getContext().getString(R.string.dialog_checking));
        mProgressDialog.show();
    }

    private void disDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
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
    void onClickUpdate() {
        Beta.checkUpgrade();
    }

    @OnClick(R.id.setting_rl_about)
    void onClickAbout() {
        mActivity.addHideFragment(this, new AboutFragment(), TAG, R.id.setting_frame_layout);
    }

    @OnClick(R.id.setting_item_text_clean_cache)
    void onClickCache() {
        GlideCatchUtil.getInstance().clearCacheDiskSelf(getContext());
        itemCleanCache.setTextSummary(mActivity.getResources().getString(R.string.setting_picture_cache_string));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MoreActivity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showDisNetWork() {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showError() {

    }
}
