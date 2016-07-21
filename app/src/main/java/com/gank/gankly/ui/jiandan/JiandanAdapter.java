package com.gank.gankly.ui.jiandan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gank.gankly.R;
import com.gank.gankly.bean.JiandanResult;
import com.gank.gankly.listener.ItemClick;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-07-20
 * Email:137387869@qq.com
 */
public class JiandanAdapter extends RecyclerView.Adapter<JiandanAdapter.JiandanHolder> {
    private List<JiandanResult.PostsBean> mList;
    private ItemClick mMeiZiOnClick;
    private Context mContext;

    public JiandanAdapter() {
        mList = new ArrayList<>();
    }

    @Override
    public JiandanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_jiandan, parent, false);
        return new JiandanHolder(view);
    }

    @Override
    public void onBindViewHolder(JiandanHolder holder, int position) {
        JiandanResult.PostsBean bean = mList.get(position);
        holder.bean = bean;
        holder.txtTitle.setText(bean.getTitle());
        holder.txtAuthor.setText(bean.getAuthor().getNickname());
        holder.txtCount.setText(String.valueOf(bean.getComment_count()));
        holder.txtTags.setText(bean.getTags().get(0).getTitle());
        Glide.with(mContext)
                .load(bean.getCustom_fields().getThumb_c().get(0))
                .into(holder.img);
    }

    public void setListener(ItemClick mMeiZiOnClick) {
        this.mMeiZiOnClick = mMeiZiOnClick;
    }

    public void updateItem(List<JiandanResult.PostsBean> list) {
        mList.clear();
        appendItem(list);
    }

    public void appendItem(List<JiandanResult.PostsBean> list) {
        mList.addAll(list);
        int position = mList.size() == 0 ? 0 : mList.size() - 1;
        notifyItemRangeInserted(position, list.size());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class JiandanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.jiandan_txt_title)
        TextView txtTitle;
        @Bind(R.id.jiandan_txt_author)
        TextView txtAuthor;
        @Bind(R.id.jiandan_txt_tags)
        TextView txtTags;
        @Bind(R.id.jiandan_txt_comment_count)
        TextView txtCount;
        @Bind(R.id.jiandan_img)
        ImageView img;

        private JiandanResult.PostsBean bean;

        public JiandanHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mMeiZiOnClick != null) {
                mMeiZiOnClick.onClick(getAdapterPosition(), bean);
            }
        }
    }
}
