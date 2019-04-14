package com.gank.gankly.widget.recyclerview;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class OnFlexibleScrollListener extends RecyclerView.OnScrollListener {

    private OnRecyclerViewListener onRecyclerViewListener;

    public OnFlexibleScrollListener() {
        super();
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                int count = recyclerView.getAdapter().getItemCount() - 1;
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == count) {
                    onLoadMore();
                }
            }
            if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager staggeredGridLayoutManager =
                        (StaggeredGridLayoutManager) layoutManager;
                int[] positions = new int[staggeredGridLayoutManager.getSpanCount()];
                staggeredGridLayoutManager.findLastVisibleItemPositions(positions);
                for (int position : positions) {
                    if (position == staggeredGridLayoutManager.getItemCount() - 1) {
                        onLoadMore();
                    }
                }
            }
            if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                if (gridLayoutManager.findLastCompletelyVisibleItemPosition() == 0) {
                    onLoadMore();
                }
            }
        }
    }

    private void onLoadMore() {
        if (onRecyclerViewListener != null) {
            onRecyclerViewListener.onLoadMore();
        }
    }

    public void setOnScrollListener(OnRecyclerViewListener listener) {
        this.onRecyclerViewListener = listener;
    }

    /**
     * 加载更多的监听
     */
    public interface OnRecyclerViewListener {
        void onLoadMore();
    }
}
