package com.gank.gankly.ui.discovered;

import android.content.Context;
import android.content.Intent;

import com.gank.gankly.R;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.main.HomeActivity;
import com.gank.gankly.ui.main.baisi.PlayerActivity;

import butterknife.OnClick;

/**
 * Create by LingYan on 2016-12-02
 * Email:137387869@qq.com
 */

public class DiscoveredMoreFragment extends LazyFragment {
    private HomeActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (HomeActivity) context;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void callBackRefreshUi() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_discovered_more;
    }

    @Override
    protected void initValues() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void bindListener() {

    }

    @OnClick(R.id.discovered_rl_budejie)
    void onClickBuDeJie() {
        mActivity.startActivity(new Intent(mActivity, PlayerActivity.class));
        mActivity.overridePendingTransition(0, 0);
    }
}
