package com.gank.gankly.ui.base;

import android.content.Context;
import android.support.annotation.NonNull;
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

    private int mCurManager = 1;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private OnSwipeRefRecyclerViewListener mOnSwipeRefRecyclerViewListener;
    private boolean isLoading = false;

    public BaseSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public BaseSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_swiperefresh, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && !isLoading
                        && mOnSwipeRefRecyclerViewListener != null) {
                    int count = mRecyclerView.getAdapter().getItemCount() - 1;
                    switch (mCurManager) {
                        case L:
                            int lastPosition = ((LinearLayoutManager) mLayoutManager).findLastCompletelyVisibleItemPosition();
                            int firstPosition = ((LinearLayoutManager) mLayoutManager).findFirstCompletelyVisibleItemPosition();
                            if (firstPosition == 0) {
                                setEnabled(true);
                            } else if (lastPosition == count) {
                                loadMore();
                            } else {
                                setEnabled(false);
                            }
                            break;
                        case S:
                            StaggeredGridLayoutManager staggeredGridLayoutManager =
                                    (StaggeredGridLayoutManager) mLayoutManager;
                            int[] first = new int[staggeredGridLayoutManager.getSpanCount()];
                            int[] last = new int[staggeredGridLayoutManager.getSpanCount()];
                            staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(last);
                            staggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(first);
                            for (int i : first) {
                                if (i == 0) {
                                    setEnabled(true);
                                    break;
                                }
                            }
                            for (int i : last) {
                                if (i == count) {
                                    loadMore();
                                    break;
                                }
                            }
                            break;
                        case G:
                            break;
                    }
                } else {
                    setEnabled(false);
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

    public void setLayoutManager(@NonNull RecyclerView.LayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
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

    /**
     * 刷新和加载更多的监听
     */
    public interface OnSwipeRefRecyclerViewListener extends OnRefreshListener {
        void onRefresh();

        void onLoadMore();
    }
}
