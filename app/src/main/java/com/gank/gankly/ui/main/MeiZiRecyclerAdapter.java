package com.gank.gankly.ui.main;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gank.gankly.R;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.widget.RatioImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-04-06
 */
public class MeiZiRecyclerAdapter extends RecyclerView.Adapter<MeiZiRecyclerAdapter.GoodsViewHolder> {
    private List<ResultsBean> mResults;
    private Activity mContext;
    private LayoutInflater inflater;

    public MeiZiRecyclerAdapter(Activity context) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        mResults = new ArrayList<>();
    }

    @Override
    public GoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_meizi, parent, false);
        return new GoodsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GoodsViewHolder holder, int position) {
        ResultsBean bean = mResults.get(position);
        holder.txtDesc.setText(bean.getDesc());
        Glide.with(mContext)
                .load(bean.getUrl())
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)

                .into(holder.imgMeizi);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        if (mResults != null) {
            return mResults.size();
        }
        return 0;
    }

    public void updateItems(List<ResultsBean> goods, boolean animated) {
        mResults.clear();
        mResults.addAll(goods);
        notifyDataSetChanged();
    }

    static class GoodsViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.meizi_txt_time)
        TextView txtDesc;

        @Bind(R.id.meizi_img_picture)
        RatioImageView imgMeizi;


        public GoodsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imgMeizi.setOriginalSize(50, 50);
        }
    }

}
