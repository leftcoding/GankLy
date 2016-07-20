package com.gank.gankly.ui.jiandan;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    public JiandanAdapter() {
        mList = new ArrayList<>();
    }

    @Override
    public JiandanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_jiandan, parent, false);
        return new JiandanHolder(view);
    }

    @Override
    public void onBindViewHolder(JiandanHolder holder, int position) {
        JiandanResult.PostsBean bean = mList.get(position);
        holder.bean = bean;
        holder.txtTitle.setText(bean.getTitle());
    }

    public void setListener(ItemClick mMeiZiOnClick) {
        this.mMeiZiOnClick = mMeiZiOnClick;
    }

    public void updateItem(List<JiandanResult.PostsBean> list) {
        mList.clear();
        appendItem(list);
    }

    public void appendItem(List<JiandanResult.PostsBean> list) {
        int size = mList.size();
        for (int i = 0; i < list.size(); i++) {
            size = size + i;
            mList.add(list.get(i));
            notifyItemInserted(size);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class JiandanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.jiandan_txt_title)
        TextView txtTitle;

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
