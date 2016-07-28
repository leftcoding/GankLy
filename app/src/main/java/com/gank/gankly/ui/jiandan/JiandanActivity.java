package com.gank.gankly.ui.jiandan;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.bean.JiandanResult;
import com.gank.gankly.config.Constants;
import com.gank.gankly.listener.ItemClick;
import com.gank.gankly.presenter.IBaseRefreshPresenter;
import com.gank.gankly.presenter.impl.JiandanPresenterImpl;
import com.gank.gankly.ui.base.BaseJiandanActivity;
import com.gank.gankly.ui.base.BaseSwipeRefreshLayout;
import com.gank.gankly.ui.web.JiandanWebActivity;
import com.gank.gankly.view.IMeiziView;
import com.gank.gankly.widget.MultipleStatusView;
import com.gank.gankly.widget.RecycleViewDivider;

import java.util.List;

import butterknife.Bind;

/**
 * 煎蛋杂谈
 * Create by LingYan on 2016-07-20
 * Email:137387869@qq.com
 */
public class JiandanActivity extends BaseJiandanActivity implements IMeiziView<List<JiandanResult.PostsBean>>,
        ItemClick {
    @Bind(R.id.main_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;
    @Bind(R.id.swipe_refresh)
    BaseSwipeRefreshLayout mSwipeRefreshLayout;

    private IBaseRefreshPresenter mPresenter;
    private static JiandanActivity sJiandanActivity;
    private JiandanAdapter mAdapter;

    public static JiandanActivity getInstance() {
        if (sJiandanActivity == null) {
            synchronized (JiandanActivity.class) {
                if (sJiandanActivity == null) {
                    sJiandanActivity = new JiandanActivity();
                }
            }
        }
        return sJiandanActivity;
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_jiandan;
    }

    @Override
    protected void initViews() {
        mToolbar.setTitle(R.string.navigation_jiandan);
        setSupportActionBar(mToolbar);
        ActionBar barLayout = getSupportActionBar();
        if (barLayout != null) {
            barLayout.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void bindListener() {
        mAdapter = new JiandanAdapter();
        mAdapter.setListener(this);
        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(this));
        mSwipeRefreshLayout.getRecyclerView().setHasFixedSize(true);
        mSwipeRefreshLayout.getRecyclerView().addItemDecoration(new RecycleViewDivider(this, R.drawable.shape_item_divider));
        mSwipeRefreshLayout.setColorSchemeColors(App.getAppColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setOnScrollListener(new BaseSwipeRefreshLayout.OnSwipeRefRecyclerViewListener() {
            @Override
            public void onRefresh() {
                mPresenter.fetchNew();
            }

            @Override
            public void onLoadMore() {
                mPresenter.fetchMore();
            }
        });
        mSwipeRefreshLayout.setAdapter(mAdapter);
    }

    @Override
    protected void initValues() {
        mMultipleStatusView.showLoading();
        mPresenter.fetchNew();
    }

    @Override
    protected void initPresenter() {
        super.initPresenter();
        mPresenter = new JiandanPresenterImpl(this, this);
    }

    @Override
    public void refillDate(List<JiandanResult.PostsBean> list) {
        mAdapter.updateItem(list);
    }

    @Override
    public void appendMoreDate(List<JiandanResult.PostsBean> list) {
        mAdapter.appendItem(list);
    }

    @Override
    public void showRefresh() {
        super.showRefresh();
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideRefresh() {
        super.hideRefresh();
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
        JiandanResult.PostsBean bean = (JiandanResult.PostsBean) object;
        Bundle bundle = new Bundle();
        bundle.putString("title", bean.getTitle());
        bundle.putString("url", bean.getUrl());
        bundle.putString("type", Constants.JIANDAN);
        bundle.putString("author", bean.getAuthor().getNickname());
//        WebActivity.startWebActivity(this, bundle);
        JiandanWebActivity.startWebActivity(this, bundle);
    }
}
