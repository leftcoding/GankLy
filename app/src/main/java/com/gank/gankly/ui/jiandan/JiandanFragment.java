package com.gank.gankly.ui.jiandan;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.bean.JiandanBean;
import com.gank.gankly.config.Constants;
import com.gank.gankly.listener.ItemClick;
import com.gank.gankly.mvp.source.remote.JiandanDataSource;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.main.HomeActivity;
import com.gank.gankly.ui.web.JiandanWebActivity;
import com.gank.gankly.utils.StyleUtils;
import com.gank.gankly.widget.LySwipeRefreshLayout;
import com.gank.gankly.widget.MultipleStatusView;
import com.gank.gankly.widget.MyDecoration;

import java.util.List;

import butterknife.BindView;

/**
 * Create by LingYan on 2016-11-18
 * Email:137387869@qq.com
 */

public class JiandanFragment extends LazyFragment implements JiandanContract.View, ItemClick {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;
    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout mSwipeRefreshLayout;

    //    private IBaseRefreshPresenter mPresenter;
    private HomeActivity mActivity;
    private JiandanAdapter mAdapter;
    private JiandanContract.Presenter mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (HomeActivity) context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_swipe_normal;
    }

    @Override
    protected void initData() {
        mMultipleStatusView.showLoading();
        mPresenter.fetchNew();
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void bindListener() {
        mAdapter = new JiandanAdapter();
        mAdapter.setListener(this);
        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(mActivity));
        mSwipeRefreshLayout.setAdapter(mAdapter);

        mSwipeRefreshLayout.getRecyclerView().setHasFixedSize(true);
        mSwipeRefreshLayout.getRecyclerView().addItemDecoration(new MyDecoration(mActivity, LinearLayoutManager.HORIZONTAL));
//        mSwipeRefreshLayout.setColorSchemeColors(App.getAppColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setOnScrollListener(new LySwipeRefreshLayout.OnSwipeRefRecyclerViewListener() {
            @Override
            public void onRefresh() {
                mPresenter.fetchNew();
            }

            @Override
            public void onLoadMore() {
                mPresenter.fetchMore();
            }
        });
    }

    @Override
    protected void initValues() {

    }

    @Override
    protected void initPresenter() {
        mPresenter = new JiandanPresenter(JiandanDataSource.getInstance(), this);
    }

    @Override
    public void changeThemes() {
        super.changeThemes();
        StyleUtils.changeSwipeRefreshLayout(mSwipeRefreshLayout);
    }

    private void changeRecyclerViewBackground() {
        int color;
        if (App.isNight()) {
            color = R.color.gray_holo_light;
        } else {
            color = R.color.gray_holo_dark;
        }
        mSwipeRefreshLayout.getRecyclerView().setBackgroundColor(App.getAppColor(color));
    }


    @Override
    public void refillData(List<JiandanBean> list) {
        mAdapter.updateItem(list);
    }

    @Override
    public void appendMoreDate(List<JiandanBean> list) {
        mAdapter.appendItem(list);
    }

    @Override
    public void showRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hasNoMoreDate() {

    }

    @Override
    public void hideRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showRefreshError(String errorStr) {

    }

    @Override
    public void showContent() {
        super.showContent();
        mMultipleStatusView.showContent();
    }

    @Override
    public void onClick(int position, Object object) {
        JiandanBean bean = (JiandanBean) object;
        Bundle bundle = new Bundle();
        bundle.putString(JiandanWebActivity.TITLE, bean.getTitle());
        bundle.putString(JiandanWebActivity.URL, bean.getUrl());
        bundle.putString(JiandanWebActivity.TYPE, Constants.JIANDAN);
        bundle.putString(JiandanWebActivity.AUTHOR, bean.getType());
        JiandanWebActivity.startWebActivity(mActivity, bundle);
    }
}
