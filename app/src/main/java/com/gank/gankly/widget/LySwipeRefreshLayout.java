package com.gank.gankly.widget;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.gank.gankly.R;
import com.socks.library.KLog;

/**
 * 组合 SwipeRefresh RecyclerView LyRecyclerView(侧滑)
 * Create by LingYan on 2016-06-23
 */
public class LySwipeRefreshLayout extends SwipeRefreshLayout {
    private static final int GESTURE_REFRESH = 2;

    private static final int L = 1;
    private static final int S = 2;
    private static final int G = 3;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView mRecyclerView;
    private OnSwipeRefreshListener mOnSwipeRefreshListener;
    private Context mContext;
    private int mCurManager = 1;
    private int mState;
    private boolean isGesture;

    public LySwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public LySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LySwipeRefreshLayout);
        try {
            mState = array.getInt(R.styleable.LySwipeRefreshLayout_refreshState, 1);
        } catch (Exception e) {
            KLog.e(e);
        } finally {
            array.recycle();
        }

        init();
    }

    private void init() {
        LayoutInflater mLayout = LayoutInflater.from(mContext);
        View view;
        if (mState == GESTURE_REFRESH) {
            view = mLayout.inflate(R.layout.view_swiperefresh, this, true);
        } else {
            view = mLayout.inflate(R.layout.layout_default_refresh, this, true);
        }
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recyclerView);

        isGesture = mRecyclerView instanceof LyRecyclerView;
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mOnSwipeRefreshListener != null
                        && !isRefreshing()) {
                    int count = recyclerView.getAdapter().getItemCount() - 1;
                    switch (mCurManager) {
                        case L:
                            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                            if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == count) {
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
        mOnSwipeRefreshListener.onLoadMore();
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

    public void setOnScrollListener(OnSwipeRefreshListener listener) {
        this.mOnSwipeRefreshListener = listener;
        super.setOnRefreshListener(listener);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }


    /**
     * 设置滑动收拾 回调监听
     *
     * @param lyRecycler
     */
    public void setILyRecycler(LyRecyclerView.ILyRecycler lyRecycler) {
        if (isGesture) {
            ((LyRecyclerView) mRecyclerView).setILyRecycler(lyRecycler);
        }
    }

    public int getPosition() {
        return ((LyRecyclerView) mRecyclerView).getPosition();
    }

    /**
     * 刷新和加载更多的监听
     */
    public interface OnSwipeRefreshListener extends OnRefreshListener {
        void onRefresh();

        void onLoadMore();
    }
}
