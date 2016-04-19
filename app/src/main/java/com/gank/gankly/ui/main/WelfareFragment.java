package com.gank.gankly.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gank.gankly.R;
import com.gank.gankly.bean.GankResult;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.config.Constants;
import com.gank.gankly.network.GankRetrofit;
import com.gank.gankly.ui.web.WebActivity;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.loadmore.LoadMoreContainer;
import in.srain.cube.views.ptr.loadmore.LoadMoreHandler;
import in.srain.cube.views.ptr.loadmore.LoadMoreListViewContainer;
import rx.Subscriber;

public class WelfareFragment extends LazyFragment {
    private static final int mLimit = 20;

    @Bind(R.id.recycler_view)
    ListView mListView;

    @Bind(R.id.welfare_frame)
    PtrClassicFrameLayout mPtrFrameLayout;

    @Bind(R.id.welfare_load_more)
    LoadMoreListViewContainer mLoadMore;

    private MainActivity mActivity;
    private GankDetailsAdapter mGankDetailsAdapter;
    private List<ResultsBean> mResults;

    private String curType = Constants.ANDROID;
    private int mPage = 1;

    public WelfareFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welfare, container, false);
        ButterKnife.bind(this, view);
        return view;
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

    private void parseArguments() {
    }

    @Override
    protected void initValues() {

    }

    @Override
    protected void initViews() {
        mResults = new ArrayList<>();
        mGankDetailsAdapter = new GankDetailsAdapter(mActivity, mResults);
        mListView.setAdapter(mGankDetailsAdapter);
    }

    @Override
    protected void bindLister() {
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                onDownRefresh();
            }
        });

        mLoadMore.useDefaultFooter();
        mLoadMore.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                mPage = mPage + 1;
                fetchDate(mPage);
            }
        });
    }

    @Override
    protected void initDate() {
        mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrameLayout.autoRefresh();
            }
        }, 150);
    }

    private void onDownRefresh() {
        mPage = 1;
        fetchDate(mPage);
    }

    private void fetchDate(int page) {
        Subscriber<GankResult> subscriber = new Subscriber<GankResult>() {
            @Override
            public void onCompleted() {
                mPtrFrameLayout.refreshComplete();
            }

            @Override
            public void onError(Throwable e) {
                KLog.e("e:" + e.toString() + "," + e);
                mPtrFrameLayout.refreshComplete();
                mLoadMore.loadMoreError(0, "刷新失败");
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
                    mLoadMore.loadMoreFinish(false, false);
                } else {
                    mLoadMore.loadMoreFinish(false, true);
                }
                mGankDetailsAdapter.notifyDataSetChanged();
            }
        };

        switch (curType) {
            case Constants.IOS:
                GankRetrofit.getInstance().fetchIos(mLimit, page, subscriber);
                break;
            case Constants.ANDROID:
                GankRetrofit.getInstance().fetchAndroid(mLimit, page, new Subscriber<GankResult>() {
                    @Override
                    public void onCompleted() {
                        mPtrFrameLayout.refreshComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e("e:" + e.toString() + "," + e);
                        mPtrFrameLayout.refreshComplete();
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
                            mLoadMore.loadMoreFinish(false, false);
                        } else {
                            mLoadMore.loadMoreFinish(false, true);
                        }
                        mGankDetailsAdapter.notifyDataSetChanged();
                    }
                });
                break;
            case Constants.ALL:
                GankRetrofit.getInstance().fetchAll(mLimit, mPage, new Subscriber<GankResult>() {
                    @Override
                    public void onCompleted() {
                        mPtrFrameLayout.refreshComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e("e:" + e.toString() + "," + e);
                        mPtrFrameLayout.refreshComplete();
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
                            mLoadMore.loadMoreFinish(false, false);
                        } else {
                            mLoadMore.loadMoreFinish(false, true);
                        }
                        mGankDetailsAdapter.notifyDataSetChanged();
                    }
                });
                break;
            default:
                break;
        }
    }

    @OnItemClick(R.id.recycler_view)
    void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("title", mResults.get(position).getDesc());
        bundle.putString("url", mResults.get(position).getUrl());
        WebActivity.startWebActivity(mActivity, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public static WelfareFragment newInstance() {
        WelfareFragment fragment = new WelfareFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

}
