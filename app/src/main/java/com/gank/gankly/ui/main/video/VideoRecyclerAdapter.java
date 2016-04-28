package com.gank.gankly.ui.main.video;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.bean.MeiziArrayList;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.ui.main.RecyclerOnClick;
import com.gank.gankly.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 视频
 * Create by LingYan on 2016-04-25
 */
public class VideoRecyclerAdapter extends RecyclerView.Adapter<VideoRecyclerAdapter.GankViewHolder> {
    private List<ResultsBean> mResults;
    private RecyclerOnClick mMeiZiOnClick;
    private Context mContext;

    public VideoRecyclerAdapter(Context context) {
        mResults = new ArrayList<>();
        mContext = context;
    }

    @Override
    public GankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_video, parent, false);
        return new GankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GankViewHolder holder, int position) {
        ResultsBean bean = mResults.get(position);
        holder.position = position;
        holder.txtDesc.setText(bean.getDesc());
        Date date = DateUtils.formatDateFromStr(bean.getPublishedAt());
        holder.txtTime.setText(App.getAppResources().getString(R.string.item_date_author,
                bean.getWho(), DateUtils.getFormatDateStr(date)));

        Glide.with(mContext)
                .load(MeiziArrayList.getInstance().getArrayList().get(position).getUrl())
                .asBitmap()
                .fitCenter()
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    public void updateItems(List<ResultsBean> getResults) {
        mResults.addAll(getResults);
        notifyDataSetChanged();
    }

    public void clear() {
        if (mResults != null) {
            mResults.clear();
        }
    }

    public List<ResultsBean> getResults() {
        return mResults;
    }

    public void setOnItemClickListener(RecyclerOnClick onItemClickListener) {
        mMeiZiOnClick = onItemClickListener;
    }

    class GankViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.goods_txt_title)
        TextView txtDesc;
        @Bind(R.id.goods_txt_author_time)
        TextView txtTime;
        @Bind(R.id.goods_img_bg)
        ImageView mImageView;
        int position;

        public GankViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            if (mMeiZiOnClick != null) {
                mMeiZiOnClick.onClick(v, position);
            }
        }
    }
}
