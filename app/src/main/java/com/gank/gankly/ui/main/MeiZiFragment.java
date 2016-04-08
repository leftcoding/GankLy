package com.gank.gankly.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gank.gankly.R;
import com.gank.gankly.base.BaseFragment;
import com.gank.gankly.bean.GankResult;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.network.GankRetrofit;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.loadmore.LoadMoreContainer;
import in.srain.cube.views.ptr.loadmore.LoadMoreHandler;
import in.srain.cube.views.ptr.loadmore.LoadMoreListViewContainer;
import rx.Subscriber;

public class MeiZiFragment extends BaseFragment {
    @Bind(R.id.recycler_view)
    ListView mListView;

    @Bind(R.id.welfare_frame)
    PtrClassicFrameLayout mPtrFrameLayout;

    @Bind(R.id.welfare_load_more)
    LoadMoreListViewContainer mLoadMore;

    private View rootView;

    private MeiZiAdapter mAdapter;
    private MainActivity mActivity;
    private List<ResultsBean> mResults;

    private boolean isCanRefresh;
    private boolean isRefreshed;

    private int mLimit = 10;
    private int mPage = 1;


    public MeiZiFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        KLog.d("onAttach");
        super.onAttach(activity);
        this.mActivity = (MainActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        KLog.d("onCreateView");
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_welfare, container, false);
            ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        KLog.d("onCreate");
        super.onCreate(savedInstanceState);
        parseArguments();
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }


    private void parseArguments() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            isCanRefresh = bundle.getBoolean("isCanRefresh", false);
        }
    }

    @Override
    protected void initValues() {
        if (isCanRefresh && !isRefreshed) {
            mPtrFrameLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPtrFrameLayout.autoRefresh();
                }
            }, 150);
        }
    }

    @Override
    protected void initViews() {
        mResults = new ArrayList<>();
        mAdapter = new MeiZiAdapter(mActivity, mResults);
        mListView.setAdapter(mAdapter);
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

    private void onDownRefresh() {
        mPage = 1;
        fetchDate(mPage);
    }

    private void fetchDate(int page) {
        GankRetrofit.getInstance().fetchWelfare(mLimit, page, new Subscriber<GankResult>() {
            @Override
            public void onCompleted() {
                mPtrFrameLayout.refreshComplete();
                isRefreshed = true;
            }

            @Override
            public void onError(Throwable e) {
                mPtrFrameLayout.refreshComplete();
            }

            @Override
            public void onNext(GankResult gankResult) {
                KLog.d("getSize:" + gankResult.getSize() + ",mPage:" + mPage);
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

                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onStop() {
        KLog.d("onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        KLog.d("onDestroyView");
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (null != rootView) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
    }

    @Override
    public void onDestroy() {
        KLog.d("onDestroy");
        super.onDestroy();
    }

    public static MeiZiFragment newInstance(boolean isCanRefresh, String type) {
        MeiZiFragment fragment = new MeiZiFragment();
        Bundle args = new Bundle();
        args.putBoolean("isCanRefresh", isCanRefresh);
        args.putString("type", type);
        fragment.setArguments(args);
        return fragment;
    }


}
