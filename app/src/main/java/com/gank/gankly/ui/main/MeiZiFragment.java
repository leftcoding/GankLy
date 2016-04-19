package com.gank.gankly.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.bean.GankResult;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.network.GankRetrofit;
import com.gank.gankly.ui.browse.BrowseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;

public class MeiZiFragment extends LazyFragment implements SwipeRefreshLayout.OnRefreshListener, MeiZiRecyclerAdapter.MeiZiOnClick {
    @Bind(R.id.meizi_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.meizi_swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private MeiZiRecyclerAdapter mRecyclerAdapter;
    private MainActivity mActivity;
    private List<ResultsBean> mResults;

    private int mLimit = 20;
    private int mPage = 1;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    public MeiZiFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (MainActivity) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseArguments();
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meizi,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    private void parseArguments() {
    }

    @Override
    protected void initValues() {
    }

    @Override
    protected void initViews() {
        initRecycler();
        mResults = new ArrayList<>();
        mRecyclerAdapter = new MeiZiRecyclerAdapter(mActivity);
        mRecyclerAdapter.setMeiZiOnClick(this);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(App.getAppColor(R.color.colorPrimary));
    }

    @Override
    protected void bindLister() {

    }

    @Override
    protected void initDate() {
        onDownRefresh();
    }

    private void onDownRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        mPage = 1;
        fetchDate(mPage);
    }

    private void initRecycler() {
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    MeiZiFragment.this.onScrollStateChanged();
                }
            }
        });
    }

    private void fetchDate(int page) {
        GankRetrofit.getInstance().fetchWelfare(mLimit, page, new Subscriber<GankResult>() {
            @Override
            public void onCompleted() {
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onNext(GankResult gankResult) {
                if (!gankResult.isEmpty()) {
                    if (mPage == 1) {
                        mResults.clear();
                    }
                    mResults.addAll(gankResult.getResults());
                }

                mRecyclerAdapter.updateItems(mResults, false);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static MeiZiFragment newInstance() {
        MeiZiFragment fragment = new MeiZiFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

//    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
//        @Override
//        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//            super.onScrolled(recyclerView, dx, dy);
//            lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
//        }
//
//        @Override
//        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//            super.onScrollStateChanged(recyclerView, newState);
//            if (newState == RecyclerView.SCROLL_STATE_IDLE
//                    && lastVisibleItem + 1 == mRecyclerAdapter.getItemCount()) {
//                mSwipeRefreshLayout.setRefreshing(true);
//                mPage = mPage + 1;
//                fetchDate(mPage);
//            }
//        }
//    };

    private void onScrollStateChanged() {
        int[] positions = new int[mStaggeredGridLayoutManager.getSpanCount()];
        mStaggeredGridLayoutManager.findLastVisibleItemPositions(positions);
        for (int position : positions) {
            if (position == mStaggeredGridLayoutManager.getItemCount() - 1) {
                mSwipeRefreshLayout.setRefreshing(true);
                mPage = mPage + 1;
                fetchDate(mPage);
                break;
            }
        }
    }

    @Override
    public void onRefresh() {
        onDownRefresh();
    }

    @Override
    public void onClick(View view, ResultsBean bean) {
        Bundle bundle = new Bundle();
        bundle.putString("url", bean.getUrl());
        Intent intent = new Intent(mActivity, BrowseActivity.class);
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
    }
}
