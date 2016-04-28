package com.gank.gankly.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gank.gankly.R;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.utils.DateUtils;
import com.gank.gankly.widget.RatioImageView;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-04-25
 */
public class GankAdapter extends RecyclerView.Adapter<GankAdapter.GankViewHolder> {
    private List<ResultsBean> mResults;
    private RecyclerOnClick mMeiZiOnClick;
    private Context mContext;

    public GankAdapter(Context context) {
        mResults = new ArrayList<>();
        mContext = context;
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

        Date date = DateUtils.formatDateFromStr(bean.getPublishedAt());
        holder.txtName.setText(bean.getWho());
        holder.txtTime.setText(DateUtils.getFormatDateStr(date));
        holder.txtFrom.setText(bean.getSource());
        Glide.with(mContext).load(R.drawable.item_left_img).centerCrop().into(holder.img);
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
        @Bind(R.id.goods_txt_author_name)
        TextView txtName;
        @Bind(R.id.goods_txt_time)
        TextView txtTime;
        @Bind(R.id.goods_txt_from)
        TextView txtFrom;
        @Bind(R.id.ri_img)
        RatioImageView img;
        int position;

        public GankViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
//            img.setOriginalSize(51,53);
        }

        @Override
        public void onClick(View v) {
            if (mMeiZiOnClick != null) {
                mMeiZiOnClick.onClick(v, position);
            }
        }
    }
}
