package com.gank.gankly.ui.collect;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gank.gankly.R;
import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.listener.ItemClick;
import com.gank.gankly.listener.ItemLongClick;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-04-11
 */
public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.CollectHolderView> {
    private List<UrlCollect> mList;
    private Context mContext;
    private ItemLongClick mItemLongClick;

    public CollectAdapter(Context context) {
        mList = new ArrayList<>();
        mContext = context;
    }

    @Override
    public CollectHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_collect, parent, false);
        return new CollectHolderView(view);
    }

    @Override
    public void onBindViewHolder(CollectHolderView holder, int position) {
        UrlCollect urlCollect = mList.get(position);
        holder.mUrlCollect = urlCollect;
        holder.title.setText(urlCollect.getComment());
    }

    public void clear() {
        mList.clear();
    }

    public void updateItems(List<UrlCollect> list) {
        mList.addAll(list);
        notifyItemRangeInserted(mList.size(), list.size());
    }

    public void deleteItem(int item) {
        mList.remove(item);
        notifyItemRemoved(item);
    }

    public void setItemLongClick(ItemClick itemLongClick) {
        mItemLongClick = (ItemLongClick) itemLongClick;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class CollectHolderView extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        @Bind(R.id.collect_txt_title)
        TextView title;
        UrlCollect mUrlCollect;

        public CollectHolderView(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            if (mItemLongClick != null) {
                mItemLongClick.onClick(getAdapterPosition(), mUrlCollect);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mItemLongClick != null) {
                mItemLongClick.onLongClick(getAdapterPosition(), mUrlCollect);
            }
            return true;
        }
    }
}
