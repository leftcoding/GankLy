package com.gank.gankly.ui.collect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gank.gankly.R;
import com.gank.gankly.config.glide.GlideRoundTransform;
import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.ui.base.BaseHolder;
import com.gank.gankly.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-04-11
 */
public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.CollectHolderView> {
    private List<UrlCollect> mList;
    private Context mContext;
    private Integer[] mImages = {R.drawable.ic_collect_default_7};
    private UrlCollect mCurrentDelete;
    private int mDeleteCurPosition;

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

        holder.time.setText(DateUtils.formatString(date, DateUtils.YY_MM_DD_HORIZONTAL));
        holder.type.setText(urlCollect.getG_type());
        holder.author.setText(urlCollect.getG_author());

        Glide.with(mContext)
                .load(mImages[0])
                .apply(new RequestOptions()
                        .transform(new GlideRoundTransform(mContext, 10))
                )
                .into(holder.userPicture);
    }

    public void updateItems(List<UrlCollect> list) {
        int size = mList.size();
        mList.clear();
        notifyItemRangeRemoved(0, size);
        addItems(list);
    }

    public void addItems(List<UrlCollect> list) {
        int size = mList.size();
        mList.addAll(list);
        notifyItemRangeInserted(size, list.size());
    }

    public void deleteItem(int position) {
        mDeleteCurPosition = position;
        mCurrentDelete = getUrlCollect(position);
        mList.remove(position);
        notifyItemRangeRemoved(position, 1);
    }

    public void backAdapter() {
        mList.add(mDeleteCurPosition, mCurrentDelete);
        notifyItemRangeInserted(mDeleteCurPosition, 1);
    }

    public UrlCollect getUrlCollect(int position) {
        return mList.get(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class CollectHolderView extends BaseHolder {
        @BindView(R.id.collect_txt_title)
        TextView title;
        @BindView(R.id.collect_txt_time)
        TextView time;
        @BindView(R.id.collect_txt_type)
        TextView type;
        @BindView(R.id.collect_user_img)
        ImageView userPicture;
        @BindView(R.id.collect_txt_author)
        TextView author;
        @BindView(R.id.collect_rl_body)
        View mView;

        UrlCollect mUrlCollect;

        public CollectHolderView(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public View getView() {
            return mView;
        }
    }
}