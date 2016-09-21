package com.gank.gankly.ui.main.mine;

import android.content.Context;
import android.os.Bundle;

import com.gank.gankly.R;
import com.gank.gankly.ui.base.BaseSwipeRefreshFragment;
import com.gank.gankly.ui.main.HomeActivity;
import com.gank.gankly.ui.main.SettingFragment;

import butterknife.OnClick;

/**
 * Create by LingYan on 2016-09-21
 * Email:137387869@qq.com
 */

public class MineFragment extends BaseSwipeRefreshFragment {
    private static MineFragment mMineFragment;
    private HomeActivity mHomeActivity;

    public static MineFragment getInstance() {
        if (mMineFragment == null) {
            mMineFragment = new MineFragment();
        }
        return mMineFragment;
    }

    public static MineFragment newInstance() {
        Bundle args = new Bundle();
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initValues() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void bindLister() {

    }

    @OnClick(R.id.mine_rl_setting)
    void onSetting() {
        mHomeActivity.addToBackFragment(SettingFragment.getInstance(), SettingFragment.TAG);
    }

    @OnClick(R.id.mine_rl_night)
    void onNight() {
    }

    @OnClick(R.id.mine_rl_collect)
    void onCollect() {
    }

    @OnClick(R.id.mine_rl_browse)
    void onBrowse() {
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHomeActivity = (HomeActivity) context;
    }
}
