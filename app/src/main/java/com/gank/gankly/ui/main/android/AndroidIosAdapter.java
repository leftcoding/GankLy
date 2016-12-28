package com.gank.gankly.ui.main.android;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gank.gankly.R;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.listener.RecyclerOnClick;
import com.gank.gankly.utils.DateUtils;
import com.gank.gankly.widget.RatioImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-04-25
 * Email:137387869@qq.com
 */
public class AndroidIosAdapter extends RecyclerView.Adapter<AndroidIosAdapter.GankViewHolder> {
    public static final int LAYOUT_Android = 1;
    public static final int LAYOUT_IOS = 2;

    private List<ResultsBean> mResults;
    private RecyclerOnClick mMeiZiOnClick;
    private Context mContext;
    public int mLayout;

    public AndroidIosAdapter(Context context) {
        this(context, LAYOUT_Android);
    }

    public AndroidIosAdapter(Context context, int type) {
        mResults = new ArrayList<>();
        mContext = context;
        mLayout = type;
    }

    @Override
    public GankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutView(parent);
        return new GankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GankViewHolder holder, int position) {
        ResultsBean bean = mResults.get(position);
        holder.mBean = bean;
        holder.txtDesc.setText(bean.getDesc());

        Date date = DateUtils.formatDateFromStr(bean.getPublishedAt());
        holder.txtTime.setText(DateUtils.getFormatDate(date, DateUtils.TYPE_DD));
        holder.txtName.setText(bean.getWho());
        if (position > holder.mSize && holder.mSize != 0) {
            position = position % holder.mSize;
        }
        if (position < holder.mSize) {
            Glide.with(mContext)
                    .load(holder.list.get(position).getUrl())
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgHead);
        }
    }

    @Override
    public void onViewRecycled(GankViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.clear(holder.imgHead);
    }

    private View getLayoutView(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        int resLayout;
        switch (mLayout) {
            case LAYOUT_IOS:
                resLayout = R.layout.adapter_ios;
                break;
            default:
                resLayout = R.layout.adapter_android;
                break;
        }
        return layoutInflater.inflate(resLayout, parent, false);
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    public void updateItems(List<ResultsBean> results) {
        int size = mResults.size();
        mResults.clear();
        notifyItemRangeRemoved(0, size);
        appendMoreDate(results);
    }

    public void appendMoreDate(List<ResultsBean> results) {
        mResults.addAll(results);
        int size = mResults.size();
        int position = size - 1 < 0 ? 0 : size - 1;
        notifyItemRangeInserted(position, results.size());
    }

    public void setOnItemClickListener(RecyclerOnClick onItemClickListener) {
        mMeiZiOnClick = onItemClickListener;
    }

    public class GankViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.goods_txt_title)
        TextView txtDesc;
        @BindView(R.id.goods_txt_author_name)
        TextView txtName;
        @BindView(R.id.goods_txt_time)
        TextView txtTime;
        @BindView(R.id.adapter_ios_ratio_img_head)
        RatioImageView imgHead;

        ResultsBean mBean;
        int mSize;
        List<ResultsBean> list;

        public GankViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            list = MeiziArrayList.getInstance().getImagesList();
            mSize = list.size();
            Collections.shuffle(list);
        }

        @Override
        public void onClick(View v) {
            if (mMeiZiOnClick != null) {
                mMeiZiOnClick.onClick(v, mBean);
            }
        }
    }
}
