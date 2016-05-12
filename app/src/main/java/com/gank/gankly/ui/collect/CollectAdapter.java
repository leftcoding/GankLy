package com.gank.gankly.ui.collect;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gank.gankly.R;
import com.gank.gankly.config.glide.GlideRoundTransform;
import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.listener.ItemClick;
import com.gank.gankly.listener.ItemLongClick;
import com.gank.gankly.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-04-11
 */
public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.CollectHolderView> {
    private final String color = "#00A0DC";
    private List<UrlCollect> mList;
    private Context mContext;
    private ItemLongClick mItemLongClick;
    private Integer[] imgs = {R.drawable.ic_collect_default_1, R.drawable.ic_collect_default_2,
            R.drawable.ic_collect_default_3, R.drawable.ic_collect_default_4,
            R.drawable.ic_collect_default_5, R.drawable.ic_collect_default_6,
            R.drawable.ic_collect_default_7};

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
        Date date = urlCollect.getDate();

        holder.time.setText(DateUtils.getFormatDate(date, DateUtils.TYPE_TWO));
        holder.type.setText(urlCollect.getG_type());
        holder.author.setText(urlCollect.getG_author());

        int index = position % imgs.length;
        Glide.with(mContext)
                .load(imgs[index])
                .transform(new GlideRoundTransform(mContext, 10))
                .into(holder.userPicture);
    }

    public void clear() {
        mList.clear();
    }

    public void updateItems(List<UrlCollect> list) {
        mList.clear();
        addItems(list);
    }

    public void addItems(List<UrlCollect> list) {
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
        @Bind(R.id.collect_txt_time)
        TextView time;
        @Bind(R.id.collect_txt_type)
        TextView type;
        @Bind(R.id.collect_user_img)
        ImageView userPicture;
        @Bind(R.id.collect_txt_author)
        TextView author;

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
