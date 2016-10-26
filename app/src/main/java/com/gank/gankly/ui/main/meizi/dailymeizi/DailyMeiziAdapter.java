package com.gank.gankly.ui.main.meizi.dailymeizi;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gank.gankly.R;
import com.gank.gankly.bean.DailyMeiziBean;
import com.gank.gankly.listener.ItemClick;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-07-05
 * Email:137387869@qq.com
 */
public class DailyMeiziAdapter extends RecyclerView.Adapter<DailyMeiziAdapter.DailyMeiziHolder> {
    private ItemClick mMeiZiOnClick;
    private List<DailyMeiziBean> mDailyMeiziBeanList;

    public DailyMeiziAdapter() {
        mDailyMeiziBeanList = new ArrayList<>();
    }

    @Override
    public DailyMeiziAdapter.DailyMeiziHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dailymeizi, parent, false);
        return new DailyMeiziHolder(view);
    }

    @Override
    public void onBindViewHolder(DailyMeiziAdapter.DailyMeiziHolder holder, int position) {
        DailyMeiziBean dailyMeiziBean = mDailyMeiziBeanList.get(position);
        holder.dailyMeiziBean = dailyMeiziBean;
        if (dailyMeiziBean != null) {
            holder.txtTitle.setText(dailyMeiziBean.getTitle());
        }
    }

    public void setOnItemClickListener(ItemClick onItemClickListener) {
        mMeiZiOnClick = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mDailyMeiziBeanList.size();
    }

    public void updateItem(List<DailyMeiziBean> dailyMeiziBeanList) {
        mDailyMeiziBeanList.clear();
        mDailyMeiziBeanList.addAll(dailyMeiziBeanList);
        notifyDataSetChanged();
    }

    public class DailyMeiziHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.daily_meizi_title)
        TextView txtTitle;
        DailyMeiziBean dailyMeiziBean;

        public DailyMeiziHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            if (mMeiZiOnClick != null) {
                mMeiZiOnClick.onClick(getAdapterPosition(), dailyMeiziBean);
            }
        }
    }
}
