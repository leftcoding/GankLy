package com.gank.gankly.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.bean.GankResult;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.config.Constants;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.listener.RecyclerOnClick;
import com.gank.gankly.network.api.GankApi;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.web.WebActivity;
import com.gank.gankly.utils.CrashUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Create by LingYan on 2016-4-26
 */
public class AndroidFragment extends LazyFragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerOnClick {
    private static final int mLimit = 20;

    @Bind(R.id.meizi_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.meizi_swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private MainActivity mActivity;
    private GankAdapter mGankAdapter;

    private int mPage = 1;
    private int mLastPosition;
    private boolean isLoadMore = true;

    public AndroidFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (MainActivity) context;
    }

    @Override
    protected void initPresenter() {

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
        mGankAdapter = new GankAdapter(mActivity);
        mGankAdapter.setOnItemClickListener(this);
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

        mRecyclerView.setItemAnimator(new ScaleInAnimator());
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mGankAdapter);
        alphaAdapter.setFirstOnly(true);
        alphaAdapter.setDuration(500);
        alphaAdapter.setInterpolator(new OvershootInterpolator(.5f));
        mRecyclerView.setAdapter(alphaAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(App.getAppColor(R.color.colorPrimary));
    }


    private void onDownRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        mPage = 1;
        fetchDate();
    }

    private void fetchDate() {
        final Observable<GankResult> androidGoods = GankApi.getInstance()
                .getGankService().fetchAndroidGoods(mLimit, mPage);
        Observable<GankResult> images = GankApi.getInstance()
                .getGankService().fetchBenefitsGoods(mLimit, mPage);

        Observable.zip(androidGoods, images, new Func2<GankResult, GankResult, GankResult>() {
            @Override
            public GankResult call(GankResult androidGoods, GankResult images) {
                MeiziArrayList.getInstance().addGiftItems(images.getResults());
                return androidGoods;
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
                        CrashUtils.crashReport(e);
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
                                mGankAdapter.clear();
                            }
                            mGankAdapter.updateItems(gankResult.getResults());
                        }

                        if (gankResult.getSize() < mLimit) {
                            Snackbar.make(mRecyclerView, R.string.tip_no_more_load, Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public static AndroidFragment newInstance() {
        AndroidFragment fragment = new AndroidFragment();
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
        bundle.putString("type", Constants.ANDROID);
        bundle.putString("author", bean.getWho());
        WebActivity.startWebActivity(mActivity, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
