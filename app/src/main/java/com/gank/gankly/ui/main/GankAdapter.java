package com.gank.gankly.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gank.gankly.R;
import com.gank.gankly.bean.ResultsBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-04-25
 */
public class GankAdapter extends RecyclerView.Adapter<GankAdapter.GankViewHolder> {
    private List<ResultsBean> mResults;
    private Context mContext;
    private LayoutInflater inflater;

    public GankAdapter(Context context) {
        mContext = context;
        mResults = new ArrayList<>();
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public GankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_welfare, parent, false);
        return new GankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GankViewHolder holder, int position) {
        ResultsBean bean = mResults.get(position);
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

    class GankViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.goods_txt_title)
        TextView txtDesc;
        @Bind(R.id.goods_txt_author_time)
        TextView txtTime;
        @Bind(R.id.goods_txt_author)
        TextView txtWho;

        public GankViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
