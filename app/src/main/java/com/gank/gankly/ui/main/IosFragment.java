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
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.listener.RecyclerOnClick;
import com.gank.gankly.network.GankRetrofit;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.web.WebActivity;
import com.socks.library.KLog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Create by LingYan on 2016-4-26
 */
public class IosFragment extends LazyFragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerOnClick {
    private static final int mLimit = 20;
    private static final String TYPE = "curType";

    @Bind(R.id.meizi_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.meizi_swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private MainActivity mActivity;
    private GankAdapter mRecyclerAdapter;

    private int mPage = 1;
    private int mLastPosition;
    private boolean isLoadMore = true;

    public IosFragment() {

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

    }

    @Override
    protected void initValues() {

    }

    @Override
    protected void initViews() {
        mRecyclerAdapter = new GankAdapter(mActivity);
        mRecyclerAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mRecyclerAdapter);
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
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && mLastPosition + 1 == mRecyclerAdapter.getItemCount() && !mSwipeRefreshLayout.isRefreshing()) {
                    if (isLoadMore) {
                        mSwipeRefreshLayout.setRefreshing(true);
                        fetchDate();
                    }
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
        final Observable<GankResult> video = GankRetrofit.getInstance()
                .getGankService().fetchIosGoods(mLimit, mPage);
        Observable<GankResult> image = GankRetrofit.getInstance()
                .getGankService().fetchBenefitsGoods(mLimit, mPage);

        Observable.zip(video, image, new Func2<GankResult, GankResult, GankResult>() {
            @Override
            public GankResult call(GankResult gankResult, GankResult gankResult2) {
                List<ResultsBean> objs = gankResult2.getResults();
                MeiziArrayList.getInstance().setArrayListSort(objs, mPage);
                return gankResult;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GankResult>() {
                    @Override
                    public void onCompleted() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mPage = mPage + 1;
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
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
                                mRecyclerAdapter.clear();
                            }
                            mRecyclerAdapter.updateItems(gankResult.getResults());
                        }

                        if (gankResult.getSize() < mLimit) {
                            Snackbar.make(mRecyclerView, R.string.tip_no_more_load, Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public static IosFragment newInstance() {
        IosFragment fragment = new IosFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onRefresh() {
        onDownRefresh();
    }

    @Override
    public void onClick(View view, ResultsBean bean) {
        Bundle bundle = new Bundle();
        bundle.putString("title", bean.getDesc());
        bundle.putString("url", bean.getUrl());
        WebActivity.startWebActivity(mActivity, bundle);
    }
}
