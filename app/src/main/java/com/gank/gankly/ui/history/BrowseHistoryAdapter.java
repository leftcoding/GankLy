package com.gank.gankly.ui.history;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gank.gankly.R;
import com.gank.gankly.data.entity.ReadHistory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-10-31
 * Email:137387869@qq.com
 */

public class BrowseHistoryAdapter extends RecyclerView.Adapter<BrowseHistoryAdapter.BrowseHolder> {
    private List<ReadHistory> mReadHistories;

    public BrowseHistoryAdapter() {
        mReadHistories = new ArrayList<>();
    }

    @Override
    public BrowseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dailymeizi, parent, false);
        return new BrowseHolder(view);
    }

    public void updateList(List<ReadHistory> readHistories) {
        mReadHistories.clear();
        appendList(readHistories);
    }

    public void appendList(List<ReadHistory> readHistories) {
        mReadHistories.addAll(readHistories);
        notifyItemRangeInserted(mReadHistories.size(), readHistories.size());
    }

    @Override
    public void onBindViewHolder(BrowseHolder holder, int position) {
        ReadHistory readHistory = mReadHistories.get(position);
        holder.mTitle.setText(readHistory.getComment());
    }

    @Override
    public int getItemCount() {
        return mReadHistories.size();
    }

    public class BrowseHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.daily_meizi_title)
        TextView mTitle;

        public BrowseHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
