package com.gank.gankly.ui.discovered.technology;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.gank.gankly.R;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.main.HomeActivity;
import com.gank.gankly.widget.LySwipeRefreshLayout;
import com.gank.gankly.widget.MultipleStatusView;

import butterknife.BindView;

/**
 * Create by LingYan on 2016-11-23
 * Email:137387869@qq.com
 */

public class TechnologyFragment extends LazyFragment {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;
    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
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
    protected int getLayoutId() {
        return R.layout.layout_swipe_normal;
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
}
