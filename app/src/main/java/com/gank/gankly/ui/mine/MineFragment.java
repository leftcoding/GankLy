package com.gank.gankly.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.gank.gankly.R;
import com.gank.gankly.butterknife.ButterKnifeFragment;
import com.gank.gankly.rxjava.RxBus_;
import com.gank.gankly.rxjava.theme.ThemeEvent;
import com.gank.gankly.ui.MainActivity;
import com.gank.gankly.ui.more.MoreActivity;
import com.gank.gankly.utils.GanklyPreferences;
import com.gank.gankly.widget.LSwitch;
import com.gank.gankly.widget.LYRelativeLayoutRipple;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

import static com.gank.gankly.ui.more.SettingFragment.IS_NIGHT;


/**
 * 我的
 * Create by LingYan on 2016-09-21
 */

public class MineFragment extends ButterKnifeFragment {
    public static final String TAG = "MineFragment";

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

    private MainActivity mActivity;
    private boolean isNight;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectItemSwitch();

        mToolbar.setTitle(R.string.navigation_mine);
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> selectTheme(isChecked));
        mNestedScrollView.setNestedScrollingEnabled(false);
    }

    private void selectTheme(boolean isChecked) {
//        AppConfig.setIsNight(isChecked);
        GanklyPreferences.putBoolean(getContext(), IS_NIGHT, isChecked);

        if (isChecked) {
            mActivity.setTheme(R.style.AppTheme_Night);
        } else {
            mActivity.setTheme(R.style.AppTheme_light);
        }
        themeSwitch.setChecked(isChecked);
        RxBus_.getInstance().post(new ThemeEvent(isChecked));
        refreshStatusBar();

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

//        setItemBackground(mViewList);

//        ButterKnife.apply(mSwitchList, new ButterKnife.Action<LSwitch>() {
//            @Override
//            public void apply(@NonNull LSwitch view, int index) {
//                view.changeTheme();
//            }
//        });
//
//        ButterKnife.apply(mTextViewList, new ButterKnife.Action<TextView>() {
//            @Override
//            public void apply(@NonNull TextView view, int index) {
//                view.setTextColor(getContext().getResources().getColor(itemTextColor));
//            }
//        });
//
//        ButterKnife.apply(mLinearLayoutCompatList, new ButterKnife.Action<LinearLayoutCompat>() {
//            @Override
//            public void apply(@NonNull LinearLayoutCompat view, int index) {
//                view.setBackgroundResource(itemBackground);
//            }
//        });
    }

    private void selectItemSwitch() {
//        boolean isNight = AppConfig.isNight();
//        themeSwitch.setChecked(isNight);
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
        openActivity(MoreActivity.TYPE_SETTING);
    }

//    public void restart() {
//        Intent intent = new Intent(mActivity, SplashActivity.class);
//        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
//        this.startActivity(intent);
//        mActivity.finish();
//    }

    @OnClick(R.id.mine_rl_night)
    void onNight() {
//        isNight = !AppConfig.isNight();
        selectTheme(isNight);
    }

    @OnClick(R.id.mine_rl_collect)
    void onCollect() {
        openActivity(MoreActivity.TYPE_COLLECT);
    }

    @OnClick(R.id.mine_rl_browse)
    void onBrowse() {
        openActivity(MoreActivity.TYPE_BROWSE);
    }

    private void openActivity(int type) {
        Intent intent = new Intent();
        intent.setClass(mActivity, MoreActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(MoreActivity.TYPE, type);
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

}
