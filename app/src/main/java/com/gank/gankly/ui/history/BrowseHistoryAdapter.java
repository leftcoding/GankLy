package com.gank.gankly.ui.history;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gank.gankly.R;
import com.gank.gankly.data.entity.ReadHistory;
import com.gank.gankly.listener.ItemClick;
import com.gank.gankly.ui.base.BaseHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-10-31
 */

public class BrowseHistoryAdapter extends RecyclerView.Adapter<BrowseHistoryAdapter.BrowseHolder> {
    private List<ReadHistory> mReadHistories;
    private ItemClick itemClick;

    public BrowseHistoryAdapter() {
        mReadHistories = new ArrayList<>();
    }

    @Override
    public BrowseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_browse_history, parent, false);
        return new BrowseHolder(view);
    }

    public void updateList(List<ReadHistory> readHistories) {
        int size = mReadHistories.size();
        mReadHistories.clear();
        notifyItemRangeRemoved(0, size);// because LinearLayoutManager use notifyItemRangeInserted,Adapter inside keep the same,must remove first
        appendList(readHistories);
    }

    public void appendList(List<ReadHistory> readHistories) {
        int size = mReadHistories.size();
        mReadHistories.addAll(readHistories);
        notifyItemRangeInserted(size, readHistories.size());
    }

    public void removeRecycle(int position) {
        mReadHistories.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(BrowseHolder holder, int position) {
        ReadHistory readHistory = mReadHistories.get(position);
        holder.mReadHistory = readHistory;
        holder.mTitle.setText(readHistory.getComment());
    }

    public void setOnItemClick(@NonNull ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    @Override
    public int getItemCount() {
        return mReadHistories.size();
    }

    public ReadHistory getReadHistory(int position) {
        return mReadHistories.get(position);
    }

    public class BrowseHolder extends BaseHolder implements View.OnClickListener {
        @BindView(R.id.title)
        TextView mTitle;
        ReadHistory mReadHistory;
        @BindView(R.id.daily_meizi_ll_body)
        public View mLinearLayout;

        public BrowseHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public View getView() {
            return mLinearLayout;
        }

        @Override
        public void onClick(View v) {
            itemClick.onClick(getAdapterPosition(), mReadHistory);
        }
    }
}
