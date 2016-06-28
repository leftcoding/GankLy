package com.gank.gankly.ui.base;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.gank.gankly.R;

/**
 * Create by LingYan on 2016-06-23
 */
public class BaseSwipeRefreshLayout extends SwipeRefreshLayout {
    private static final int L = 1;
    private static final int S = 2;
    private static final int G = 3;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView mRecyclerView;
    private OnSwipeRefRecyclerViewListener mOnSwipeRefRecyclerViewListener;
    private Context mContext;
    private int mCurManager = 1;
    private boolean isLoading = false;

    public BaseSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public BaseSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_swiperefresh, this, true);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recyclerView);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mOnSwipeRefRecyclerViewListener != null
                        && !isRefreshing()) {
                    int count = recyclerView.getAdapter().getItemCount() - 1;
                    switch (mCurManager) {
                        case L:
                            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                            if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == count) {
                                loadMore();
                            }
                            break;
                        case G:
                            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                            if (gridLayoutManager.findLastCompletelyVisibleItemPosition() == 0) {
                                loadMore();
                            }
                            break;
                        case S:
                            StaggeredGridLayoutManager staggeredGridLayoutManager =
                                    (StaggeredGridLayoutManager) layoutManager;
//                            int[] first = new int[staggeredGridLayoutManager.getSpanCount()];
//                            int[] last = new int[staggeredGridLayoutManager.getSpanCount()];
//                            staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(last);
//                            staggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(first);
//                            for (int i : last) {
//                                if (i == count) {
//                                    loadMore();
//                                    break;
//                                }
//                            }
                            int[] positions = new int[staggeredGridLayoutManager.getSpanCount()];
                            staggeredGridLayoutManager.findLastVisibleItemPositions(positions);
                            for (int position : positions) {
                                if (position == staggeredGridLayoutManager.getItemCount() - 1) {
                                    loadMore();
                                    break;
                                }
                            }
                            break;
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    public void loadMore() {
        isLoading = true;
        mOnSwipeRefRecyclerViewListener.onLoadMore();
    }

    public void finishLoad() {
        isLoading = false;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        if (layoutManager instanceof LinearLayoutManager) {
            mCurManager = L;
        } else if (layoutManager instanceof GridLayoutManager) {
            mCurManager = G;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            mCurManager = S;
        }
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public void setRefreshListener(OnSwipeRefRecyclerViewListener listener) {
        this.mOnSwipeRefRecyclerViewListener = listener;
        super.setOnRefreshListener(listener);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * 刷新和加载更多的监听
     */
    public interface OnSwipeRefRecyclerViewListener extends OnRefreshListener {
        void onRefresh();

        void onLoadMore();
    }
}
