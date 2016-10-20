package com.gank.gankly.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.RxBus.ChangeThemeEvent.ThemeEvent;
import com.gank.gankly.RxBus.RxBus;
import com.gank.gankly.ui.base.BaseSwipeRefreshFragment;
import com.gank.gankly.ui.main.HomeActivity;
import com.gank.gankly.ui.more.SettingActivity;
import com.gank.gankly.utils.GanklyPreferences;
import com.gank.gankly.widget.LSwitch;
import com.gank.gankly.widget.LYRelativeLayoutRipple;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.gank.gankly.ui.more.SettingFragment.IS_NIGHT;


/**
 * Create by LingYan on 2016-09-21
 * Email:137387869@qq.com
 */

public class MineFragment extends BaseSwipeRefreshFragment {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.mine_ls_theme)
    LSwitch themeSwitch;
    @BindView(R.id.mine_nested_scroll)
    NestedScrollView mNestedScrollView;
    @BindViews({R.id.mine_txt_browse, R.id.mine_txt_setting, R.id.mine_txt_collect, R.id.mine_txt_night})
    List<TextView> mTextViewList;
    @BindViews({R.id.mine_ls_theme})
    List<LSwitch> mSwitchList;
    @BindViews({R.id.mine_ll_notes, R.id.mine_ll_setting})
    List<LinearLayoutCompat> mLinearLayoutCompatList;
    @BindViews({R.id.mine_rl_collect, R.id.mine_rl_night, R.id.mine_rl_setting, R.id.mine_rl_browse})
    List<LYRelativeLayoutRipple> mViewList;

    private HomeActivity mActivity;
    private boolean isNight;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initValues() {
        selectItemSwitch();

        mNestedScrollView.setNestedScrollingEnabled(false);
    }

    @Override
    protected void initViews() {
        mToolbar.setTitle(R.string.navigation_mine);
    }

    @Override
    protected void bindLister() {
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectTheme(isChecked);
            }
        });
    }

    private void selectTheme(boolean isChecked) {
        App.setIsNight(isChecked);
        GanklyPreferences.putBoolean(IS_NIGHT, isChecked);

        if (isChecked) {
            mActivity.setTheme(R.style.AppTheme_Night);
        } else {
            mActivity.setTheme(R.style.AppTheme_Day);
        }
        themeSwitch.setChecked(isChecked);
        RxBus.getInstance().post(new ThemeEvent(isChecked));
        refreshStatusBar();
        onRefreshUi();
    }

    private void selectItemSwitch() {
        boolean isNight = App.isNight();
        themeSwitch.setChecked(isNight);
    }

    private void onRefreshUi() {
        TypedValue typeValue = new TypedValue();
        Resources.Theme theme = mActivity.getTheme();
        theme.resolveAttribute(R.attr.themeBackground, typeValue, true);
        mNestedScrollView.setBackgroundResource(typeValue.resourceId);
        theme.resolveAttribute(R.attr.colorPrimary, typeValue, true);
        mToolbar.setBackgroundResource(typeValue.resourceId);
        theme.resolveAttribute(R.attr.textPrimaryColor, typeValue, true);
        final int itemTextColor = typeValue.resourceId;
        theme.resolveAttribute(R.attr.baseAdapterItemBackground, typeValue, true);
        final int itemBackground = typeValue.resourceId;

        setItemBackground(mViewList);

        ButterKnife.apply(mSwitchList, new ButterKnife.Action<LSwitch>() {
            @Override
            public void apply(@NonNull LSwitch view, int index) {
                view.changeTheme();
            }
        });

        ButterKnife.apply(mTextViewList, new ButterKnife.Action<TextView>() {
            @Override
            public void apply(@NonNull TextView view, int index) {
                view.setTextColor(App.getAppColor(itemTextColor));
            }
        });

        ButterKnife.apply(mLinearLayoutCompatList, new ButterKnife.Action<LinearLayoutCompat>() {
            @Override
            public void apply(@NonNull LinearLayoutCompat view, int index) {
                view.setBackgroundResource(itemBackground);
            }
        });
    }

    /**
     * 刷新 StatusBar
     */
    private void refreshStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            TypedValue typedValue = new TypedValue();
            mActivity.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
            mActivity.getWindow().setStatusBarColor(getResources().getColor(typedValue.resourceId));
        }
    }

    @OnClick(R.id.mine_rl_setting)
    void onSetting() {
        Intent intent = new Intent();
        intent.setClass(mActivity, SettingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(SettingActivity.TITLE, App.getAppString(R.string.navigation_settings));
        bundle.putInt(SettingActivity.TYPE, SettingActivity.TYPE_SETTING);
        intent.putExtras(bundle);
        goActivity(intent);
    }

    @OnClick(R.id.mine_rl_night)
    void onNight() {
        isNight = !App.isNight();
        selectTheme(isNight);
    }

    @OnClick(R.id.mine_rl_collect)
    void onCollect() {
        Intent intent = new Intent();
        intent.setClass(mActivity, SettingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(SettingActivity.TITLE, App.getAppString(R.string.mine_my_collect));
        bundle.putInt(SettingActivity.TYPE, SettingActivity.TYPE_COLLECT);
        intent.putExtras(bundle);
        goActivity(intent);
    }

    @OnClick(R.id.mine_rl_browse)
    void onBrowse() {
    }

    private void goActivity(Intent intent) {
        if (intent != null) {
            mActivity.startActivity(intent);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (HomeActivity) context;
    }
}
