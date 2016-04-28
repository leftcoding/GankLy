package com.gank.gankly.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.bean.GankResult;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.config.Constants;
import com.gank.gankly.network.GankRetrofit;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.web.WebActivity;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;

/**
 * Create by LingYan on 2016-4-26
 */
public class WelfareFragment extends LazyFragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerOnClick {
    private static final int mLimit = 20;
    private static final String TYPE = "curType";

    @Bind(R.id.meizi_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.meizi_swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private MainActivity mActivity;
    private GankAdapter mGankAdapter;
    private List<ResultsBean> mResults;

    private String curType = Constants.ANDROID;
    private int mPage = 1;
    private int mLastPosition;

    public WelfareFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseArguments();
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    private void parseArguments() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(TYPE)) {
                curType = bundle.getString(TYPE);
            }
        }
    }

    @Override
    protected void initValues() {

    }

    @Override
    protected void initViews() {
        mResults = new ArrayList<>();
        mGankAdapter = new GankAdapter(mActivity);
        mGankAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mGankAdapter);
        initRecycler();
    }

    @Override
    protected void bindLister() {

    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_meizi;
    }

    @Override
    protected void initDate() {
        onDownRefresh();
    }

    private void initRecycler() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
//        mRecyclerView.addItemDecoration(new RecycleViewDivider(mActivity, R.drawable.shape_item_divider));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && mLastPosition + 1 == mGankAdapter.getItemCount() && !mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    fetchDate();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(App.getAppColor(R.color.colorPrimary));
    }


    private void onDownRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        mPage = 1;
        fetchDate();
    }

    private void fetchDate() {
        Subscriber<GankResult> subscriber = new Subscriber<GankResult>() {
            @Override
            public void onCompleted() {
                mSwipeRefreshLayout.setRefreshing(false);
                mPage = mPage + 1;
            }

            @Override
            public void onError(Throwable e) {
                KLog.e("e:" + e.toString() + "," + e);
                mSwipeRefreshLayout.setRefreshing(false);
                Snackbar.make(mSwipeRefreshLayout, R.string.tip_server_error, Snackbar.LENGTH_LONG)
                        .setActionTextColor(App.getAppColor(R.color.Blue))
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onDownRefresh();
                            }
                        }).show();
            }

            @Override
            public void onNext(GankResult gankResult) {
                if (!gankResult.isEmpty()) {
                    if (mPage == 1) {
                        mResults.clear();
                    }
                    mResults.addAll(gankResult.getResults());
                }

                if (gankResult.getSize() < mLimit) {

                } else {

                }
                mGankAdapter.updateItems(mResults);
            }
        };
        switch (curType) {
            case Constants.ANDROID:
                GankRetrofit.getInstance().fetchAndroid(mLimit, mPage, subscriber);
                break;
            case Constants.IOS:
                GankRetrofit.getInstance().fetchIos(mLimit, mPage, subscriber);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public static WelfareFragment newInstance(String curType) {
        WelfareFragment fragment = new WelfareFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, curType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onRefresh() {
        onDownRefresh();
    }

    @Override
    public void onClick(View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("title", mResults.get(position).getDesc());
        bundle.putString("url", mResults.get(position).getUrl());
        WebActivity.startWebActivity(mActivity, bundle);
    }
}
