package com.gank.gankly.ui.collect;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gank.gankly.R;
import com.gank.gankly.data.entity.UrlCollect;

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
        holder.title.setText(urlCollect.getComment());
    }

    public void clear() {
        mList.clear();
    }

    public void updateItems(List<UrlCollect> list) {
        mList.addAll(list);
        notifyItemInserted(mList.size());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class CollectHolderView extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.collect_txt_title)
        TextView title;

        public CollectHolderView(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
