package com.gank.gankly.ui.main.meizi;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gank.gankly.R;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.listener.MeiziOnClick;
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

    private MeiziOnClick mMeiZiOnClick;

    public void setMeiZiOnClick(MeiziOnClick meiZiOnClick) {
        mMeiZiOnClick = meiZiOnClick;
    }

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
        holder.position = position;
        holder.imgMeizi.setMinimumHeight(500 + (int) (200 * Math.random()));
        Glide.with(mContext)
                .load(bean.getUrl())
//                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
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

    public void updateItems(List<ResultsBean> goods) {
        clear();
        addItems(goods);
    }

    public void clear() {
        if (mResults != null) {
            mResults.clear();
        }
    }

    public void addItems(List<ResultsBean> goods) {
        mResults.addAll(goods);
        int position = mResults.size() == 0 ? 0 : mResults.size() - 1;
        notifyItemRangeInserted(position, goods.size());
    }

    class GoodsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.meizi_img_picture)
        RatioImageView imgMeizi;
        int position;

        public GoodsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            imgMeizi.setOriginalSize(50, 50);
            imgMeizi.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mMeiZiOnClick != null) {
                mMeiZiOnClick.onClick(v, position);
            }
        }
    }

}
