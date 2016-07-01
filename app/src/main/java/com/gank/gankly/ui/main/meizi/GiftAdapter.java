package com.gank.gankly.ui.main.meizi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gank.gankly.R;
import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.listener.ItemClick;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-04-25
 */
public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.GankViewHolder> {
    private List<GiftBean> mResults;
    private ItemClick mMeiZiOnClick;
    private Context mContext;

    public GiftAdapter(Context context) {
        mResults = new ArrayList<>();
        mContext = context;
    }

    @Override
    public GankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_video, parent, false);
        return new GankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GankViewHolder holder, int position) {
        final GiftBean bean = mResults.get(position);
        holder.mGiftBean = bean;
        holder.position = position;
        holder.txtDesc.setText(bean.getTitle());
        holder.txtAuthor.setText(bean.getTime());
        Glide.with(mContext)
                .load(bean.getImgUrl())
                .asBitmap()
                .fitCenter()
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    public void updateItems(List<GiftBean> getResults) {
        int size = mResults.size();
        for (int i = 0; i < getResults.size(); i++) {
            size = size + i;
            mResults.add(getResults.get(i));
            notifyItemInserted(size);
        }
    }

    public void clear() {
        if (mResults != null) {
            mResults.clear();
        }
    }

    public void setOnItemClickListener(ItemClick onItemClickListener) {
        mMeiZiOnClick = onItemClickListener;
    }

    class GankViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.goods_txt_title)
        TextView txtDesc;
        @Bind(R.id.goods_img_bg)
        ImageView mImageView;
        @Bind(R.id.video_txt_author)
        TextView txtAuthor;
        int position;
        GiftBean mGiftBean;

        public GankViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            if (mMeiZiOnClick != null) {
                mMeiZiOnClick.onClick(position, mGiftBean);
            }
        }
    }
}
