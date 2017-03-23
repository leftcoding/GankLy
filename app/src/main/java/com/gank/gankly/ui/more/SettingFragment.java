package com.gank.gankly.ui.more;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.gank.gankly.App;
import com.gank.gankly.R;
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
import butterknife.OnClick;

/**
 * 设置
 * Create by LingYan on 2016-05-10
 * Email:137387869@qq.com
 */
public class SettingFragment extends BaseSwipeRefreshFragment implements ILauncher {
    public static final String TAG = "SettingFragment";

    public static final String IS_NIGHT = "isNight";
    private static final String DIALOG_TAG = "versionDialog";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.setting_rl_body)
    View mView;
    @BindView(R.id.setting_switch_check)
    ItemSwitchView mAutoCheckSwitch;
    @BindView(R.id.setting_item_text_update)
    ItemTextView itemUpdate;
    @BindView(R.id.setting_text_copyright)
    TextView txtCopyRight;
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
    protected void initPresenter() {
        mPresenter = new LauncherPresenter(mActivity, this);
    }

    @Override
    protected void initValues() {
    }

    @Override
    protected void initViews() {
        initPreferences();

        mToolbar.setTitle(R.string.navigation_settings);
        mActivity.setSupportActionBar(mToolbar);
        ActionBar bar = mActivity.getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(v -> mActivity.onBackPressed());
    }

    private void initPreferences() {
        String summary = App.getAppResources().getString(R.string.setting_current_version,
                AppUtils.getVersionName(mActivity));
        itemUpdate.setTextSummary(summary);
        itemUpdate.setTextName(R.string.setting_check_version);

        if (false) {
            itemUpdate.showVersion();
        }

        selectItemSwitch();
    }

    private void selectItemSwitch() {
        boolean isAutoCheck = GanklyPreferences.getBoolean(Preferences.SETTING_AUTO_CHECK, true);
        mAutoCheckSwitch.setSwitchChecked(isAutoCheck);
    }

    @Override
    protected void bindListener() {
        mAutoCheckSwitch.setSwitchListener(isCheck -> savePreferences(isCheck));

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


    @Override
    public void showDialog() {
        createDialog();
    }

    @Override
    public void hiddenDialog() {
        disDialog();
    }

    @OnClick(R.id.setting_item_text_update)
    void clickUpdate() {
        mPresenter.checkVersion();
    }

    @OnClick(R.id.setting_rl_about)
    void onClickAbout() {
        mActivity.addHideFragment(this, new AboutFragment(), TAG, R.id.setting_frame_layout);
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
    protected void callBackRefreshUi() {

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

    @Override
    public void showLoading() {

    }
}
