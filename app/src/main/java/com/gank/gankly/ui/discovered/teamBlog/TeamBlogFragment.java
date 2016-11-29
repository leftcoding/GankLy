package com.gank.gankly.ui.discovered.teamBlog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gank.gankly.R;
import com.gank.gankly.bean.JianDanBean;
import com.gank.gankly.config.Constants;
import com.gank.gankly.listener.ItemClick;
import com.gank.gankly.mvp.source.remote.TeamBlogDataSource;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.discovered.technology.TechnologyContract;
import com.gank.gankly.ui.main.HomeActivity;
import com.gank.gankly.ui.web.JiandanWebActivity;
import com.gank.gankly.ui.web.normal.WebActivity;
import com.gank.gankly.utils.theme.RecyclerViewColor;
import com.gank.gankly.utils.theme.ThemeColor;
import com.gank.gankly.widget.LySwipeRefreshLayout;
import com.gank.gankly.widget.MultipleStatusView;

import java.util.List;

import butterknife.BindView;

/**
 * Create by LingYan on 2016-11-23
 * Email:137387869@qq.com
 */

public class TeamBlogFragment extends LazyFragment implements TechnologyContract.View, ItemClick {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;
    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private HomeActivity mActivity;
    private TeamBlogAdapter mAdapter;

    private TechnologyContract.Presenter mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (HomeActivity) context;
    }

    @Override
    protected void initData() {
        showRefresh();
        mPresenter.fetchNew();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new TeamBlogPresenter(TeamBlogDataSource.getInstance(), this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_swipe_normal;
    }

    @Override
    protected void initValues() {
        setSwipeRefreshLayout(mSwipeRefreshLayout);
        setMultipleStatusView(mMultipleStatusView);

        mAdapter = new TeamBlogAdapter();
        mAdapter.setListener(this);
        mSwipeRefreshLayout.setAdapter(mAdapter);

        mRecyclerView = mSwipeRefreshLayout.getRecyclerView();
        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(mActivity));
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
    protected void initViews() {

    }

    @Override
    protected void bindListener() {

    }

    @Override
    public void showRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void hasNoMoreDate() {

    }

    @Override
    public void showRefreshError(String errorStr) {

    }

    @Override
    public void refillData(List<JianDanBean> list) {
        mAdapter.updateItem(list);
    }

    @Override
    public void appendData(List<JianDanBean> list) {
        mAdapter.appendItem(list);
    }

    @Override
    public void onClick(int position, Object object) {
        JianDanBean bean = (JianDanBean) object;
        Bundle bundle = new Bundle();
        bundle.putString(WebActivity.TITLE, bean.getTitle());
        bundle.putString(WebActivity.URL, bean.getUrl());
        bundle.putString(WebActivity.TYPE, Constants.TECHNOLOGY);
        bundle.putString(WebActivity.AUTHOR, "");
        Intent intent = new Intent(mActivity, WebActivity.class);
        intent.putExtras(bundle);
        JiandanWebActivity.startWebActivity(mActivity, bundle);
    }

    @Override
    protected void callBackRefreshUi() {
        RecyclerViewColor recyclerViewColor = new RecyclerViewColor(mRecyclerView);
        recyclerViewColor.backGroundColor(R.id.team_ll_body, R.attr.lyItemSelectBackground);
        recyclerViewColor.textViewColor(R.id.team_blog_txt_title, R.attr.baseAdapterItemTextColor);

        ThemeColor themeColor = new ThemeColor(this);
        themeColor.backgroundResource(R.attr.themeBackground, mRecyclerView);
        themeColor.swipeRefresh(mSwipeRefreshLayout);
        themeColor.recyclerViewColor(recyclerViewColor);
        themeColor.start();
    }
}
