package com.gank.gankly.ui.dailymeizi;

import android.ly.business.domain.DailyMeizi;
import android.ly.business.domain.DailyMeiziBean;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gank.gankly.R;
import com.gank.gankly.listener.ItemClick;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-07-05
 */
public class DailyMeiziAdapter extends RecyclerView.Adapter<DailyMeiziAdapter.DailyMeiziHolder> {
    private ItemClick mMeiZiOnClick;
    private List<DailyMeizi> mDailyMeiziList;

    DailyMeiziAdapter() {
        setHasStableIds(true);
        mDailyMeiziList = new ArrayList<>();
    }

    @Override
    public DailyMeiziAdapter.DailyMeiziHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dailymeizi, parent, false);
        return new DailyMeiziHolder(view);
    }

    @Override
    public void onBindViewHolder(DailyMeiziAdapter.DailyMeiziHolder holder, int position) {
        DailyMeizi dailyMeizi = mDailyMeiziList.get(position);
        holder.dailyMeizi = dailyMeizi;
        if (dailyMeizi != null) {
            holder.txtTitle.setText(dailyMeizi.getTitle());
        }
    }

    public void setOnItemClickListener(ItemClick onItemClickListener) {
        mMeiZiOnClick = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mDailyMeiziList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    void refillItem(List<DailyMeizi> dailyMeiziList) {
        int size = mDailyMeiziList.size();
        mDailyMeiziList.clear();
        notifyItemRangeRemoved(0, size);
        appendItem(dailyMeiziList);
    }

    public void appendItem(List<DailyMeizi> dailyMeiziList) {
        mDailyMeiziList.addAll(dailyMeiziList);
        notifyItemRangeInserted(getItemCount(), dailyMeiziList.size());
    }

    public class DailyMeiziHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.daily_meizi_title)
        TextView txtTitle;
        DailyMeizi dailyMeizi;

        DailyMeiziHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            if (mMeiZiOnClick != null) {
                mMeiZiOnClick.onClick(getAdapterPosition(), dailyMeizi);
            }
        }
    }
}
