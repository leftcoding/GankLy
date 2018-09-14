package com.gank.gankly.ui.discovered.more;

import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;

import com.gank.gankly.R;
import com.gank.gankly.ui.MainActivity;
import com.gank.gankly.ui.baisi.BaiSiActivity;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.utils.theme.ThemeColor;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by LingYan on 2016-12-02
 */

public class DiscoveredMoreFragment extends LazyFragment {
    @BindView(R.id.discovered_parent)
    LinearLayout mLinearLayout;

    private MainActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    protected void callBackRefreshUi() {
        ThemeColor color = new ThemeColor(this);
        color.setBackgroundResource(R.attr.themeBackground, mLinearLayout);
        color.start();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_discovered_more;
    }

    @OnClick(R.id.discovered_rl_budejie)
    void onClickBuDeJie() {
        mActivity.startActivity(new Intent(mActivity, BaiSiActivity.class));
        mActivity.overridePendingTransition(0, 0);
    }

    @Override
    public void onLazyActivityCreate() {

    }
}
