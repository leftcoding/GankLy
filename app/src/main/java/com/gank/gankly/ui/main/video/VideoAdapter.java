package com.gank.gankly.ui.main.video;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gank.gankly.R;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.listener.MeiziOnClick;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 视频
 * Create by LingYan on 2016-04-25
 * Email:137387869@qq.com
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.GankViewHolder> {
    private List<ResultsBean> mResults;
    private MeiziOnClick mMeiZiOnClick;
    private Context mContext;

    public VideoAdapter(Context context) {
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
        holder.txtDesc.setText(bean.getDesc());
        holder.txtAuthor.setText(bean.getWho());

        int size = MeiziArrayList.getInstance().getArrayList().size();
        if (position > size && size > 0) {
            position = position % size;
        }
        if (position < size) {
            Glide.with(mContext)
                    .load(MeiziArrayList.getInstance().getArrayList().get(position).getUrl())
                    .asBitmap()
                    .fitCenter()
                    .into(holder.mImageView);
        }
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    public void updateItems(List<ResultsBean> getResults) {
        clear();
        addItems(getResults);
    }

    public void addItems(List<ResultsBean> getResults) {
        mResults.addAll(getResults);
        notifyItemRangeInserted(mResults.size() , getResults.size());
    }

    public void clear() {
        if (mResults != null) {
            mResults.clear();
        }
    }

    public List<ResultsBean> getResults() {
        return mResults;
    }

    public void setOnItemClickListener(MeiziOnClick onItemClickListener) {
        mMeiZiOnClick = onItemClickListener;
    }

    class GankViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.goods_txt_title)
        TextView txtDesc;
        @BindView(R.id.goods_img_bg)
        ImageView mImageView;
        @BindView(R.id.video_txt_author)
        TextView txtAuthor;

        public GankViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            if (mMeiZiOnClick != null) {
                mMeiZiOnClick.onClick(v, getAdapterPosition());
            }
        }
    }
}
