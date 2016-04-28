package com.gank.gankly.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gank.gankly.R;
import com.gank.gankly.bean.ResultsBean;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-04-25
 */
public class GankAdapter extends RecyclerView.Adapter<GankAdapter.GankViewHolder> {
    private List<ResultsBean> mResults;
    private RecyclerOnClick mMeiZiOnClick;

    public GankAdapter() {
        mResults = new ArrayList<>();
    }

    @Override
    public GankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_welfare, parent, false);
        return new GankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GankViewHolder holder, int position) {
        ResultsBean bean = mResults.get(position);
        holder.position = position;
        holder.txtDesc.setText(bean.getDesc());
        holder.txtTime.setText(bean.getPublishedAt());
        holder.txtWho.setText(bean.getWho());
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    public void updateItems(List<ResultsBean> results) {
        mResults = results;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(RecyclerOnClick onItemClickListener) {
        KLog.d("setOnItemClickListener");
        mMeiZiOnClick = onItemClickListener;
    }

    class GankViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.goods_txt_title)
        TextView txtDesc;
        @Bind(R.id.goods_txt_author_time)
        TextView txtTime;
        @Bind(R.id.goods_txt_author)
        TextView txtWho;
        int position;

        public GankViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            KLog.d("onClick");
            if (mMeiZiOnClick != null) {
                mMeiZiOnClick.onClick(v, position);
            }
        }
    }
}
