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
import com.gank.gankly.bean.JiandanBean;
import com.gank.gankly.listener.ItemClick;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-07-20
 * Email:137387869@qq.com
 */
public class JiandanAdapter extends RecyclerView.Adapter<JiandanAdapter.JiandanHolder> {
    private List<JiandanBean> mList;
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
        JiandanBean bean = mList.get(position);
        holder.bean = bean;
        holder.txtTitle.setText(bean.getTitle());
        holder.txtAuthor.setText(bean.getType());
        holder.txtCount.setText("");
        holder.txtTags.setText("");

        Glide.with(mContext)
                .load(bean.getImgUrl())
                .into(holder.img);
    }

    public void setListener(ItemClick mMeiZiOnClick) {
        this.mMeiZiOnClick = mMeiZiOnClick;
    }

    public void updateItem(List<JiandanBean> list) {
        mList.clear();
        appendItem(list);
    }

    public void appendItem(List<JiandanBean> list) {
        mList.addAll(list);
        notifyItemRangeInserted(mList.size(), list.size());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class JiandanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.jiandan_txt_title)
        TextView txtTitle;
        @BindView(R.id.jiandan_txt_author)
        TextView txtAuthor;
        @BindView(R.id.jiandan_txt_tags)
        TextView txtTags;
        @BindView(R.id.jiandan_txt_comment_count)
        TextView txtCount;
        @BindView(R.id.jiandan_img)
        ImageView img;

        private JiandanBean bean;

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
