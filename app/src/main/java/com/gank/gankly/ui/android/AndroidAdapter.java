package com.gank.gankly.ui.android;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gank.gankly.R;
import com.gank.gankly.listener.RecyclerOnClick;
import com.gank.gankly.ui.base.BaseAdapter;
import com.gank.gankly.ui.base.ButterHolder;
import com.gank.gankly.utils.DateUtils;
import com.leftcoding.http.bean.ResultsBean;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

import static com.gank.gankly.ui.android.AndroidAdapter.DefaultItem.VIEW_DEFAULT;


/**
 * Create by LingYan on 2016-04-25
 */
class AndroidAdapter extends BaseAdapter<AndroidAdapter.DefaultHolder> {
    private RecyclerOnClick mRecyclerOnClick;
    private Context mContext;

    private List<ResultsBean> mResultsBeans;
    private List<DefaultItem> mItemList;

    AndroidAdapter(@NonNull Context context) {
        setHasStableIds(true);
        mContext = context;
        mResultsBeans = new ArrayList<>();
        mItemList = new ArrayList<>();
        registerAdapterDataObserver(dataObserver);
    }

    private final RecyclerView.AdapterDataObserver dataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            mItemList.clear();
            int size = getItemCount();
            if (size > 0) {
                for (ResultsBean resultsBean : mResultsBeans) {
                    if (resultsBean != null) {
                        mItemList.add(new TextItem(resultsBean));
                    }
                }
            }
        }
    };

    @Override
    public int getItemViewType(int position) {
        if (mItemList != null && !mItemList.isEmpty()) {
            return mItemList.get(position).getType();
        }
        return -1;
    }

    @Override
    public DefaultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DefaultHolder defaultHolder;
        switch (viewType) {
            case VIEW_DEFAULT:
                defaultHolder = new TextHolder(mContext, parent);
                break;
            default:
                defaultHolder = null;
                break;
        }
        return defaultHolder;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(DefaultHolder holder, int position) {
        if (holder instanceof TextHolder) {
            holder.bindItem(mContext, mItemList.get(position));
        }
    }

    @Override
    public void onViewRecycled(DefaultHolder holder) {
        super.onViewRecycled(holder);
        Glide.get(mContext).clearMemory();
    }

    @Override
    public int getItemCount() {
        return mResultsBeans == null ? 0 : mResultsBeans.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    void fillItems(List<ResultsBean> results) {
        mResultsBeans.clear();
        appendItems(results);
    }

    public void appendItems(List<ResultsBean> results) {
        mResultsBeans.addAll(results);
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener(RecyclerOnClick onItemClickListener) {
        mRecyclerOnClick = onItemClickListener;
    }

    @Override
    public void destroy() {
        if (mItemList != null) {
            mItemList.clear();
            mItemList = null;
        }

        if (mResultsBeans != null) {
            mResultsBeans.clear();
            mResultsBeans = null;
        }

        unregisterAdapterDataObserver(dataObserver);
    }

    private class TextItem implements DefaultItem {

        private ResultsBean mResultsBean;

        @Override
        public int getType() {
            return VIEW_DEFAULT;
        }

        TextItem(ResultsBean resultsBean) {
            this.mResultsBean = resultsBean;
        }

        ResultsBean getResultsBean() {
            return mResultsBean;
        }

        public String getTime() {
            Date date = DateUtils.formatToDate(mResultsBean.publishedAt);
            return DateUtils.formatString(date, DateUtils.MM_DD);
        }
    }

    class TextHolder extends DefaultHolder<TextItem> {
        @BindView(R.id.author_name)
        TextView authorName;

        @BindView(R.id.time)
        TextView time;

        @BindView(R.id.title)
        TextView title;

        TextHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.adapter_android);
        }

        @Override
        public void bindItem(Context context, TextItem item) {
            final ResultsBean resultsBean = item.getResultsBean();

            time.setText(item.getTime());
            title.setText(resultsBean.desc);
            authorName.setText(resultsBean.getWho());

            itemView.setOnClickListener(v -> {
                if (mRecyclerOnClick != null) {
                    mRecyclerOnClick.onClick(v, getAdapterPosition(), resultsBean);
                }
            });
        }
    }

    static abstract class DefaultHolder<II extends DefaultItem> extends ButterHolder {

        DefaultHolder(Context context, ViewGroup parent, @LayoutRes int layoutID) {
            super(context, parent, layoutID);
        }

        public abstract void bindItem(Context context, II item);
    }

    interface DefaultItem {
        int VIEW_DEFAULT = 0;

        @IntDef({
                VIEW_DEFAULT
        })

        @Retention(RetentionPolicy.SOURCE)
        @interface ViewType {
        }

        @ViewType
        int getType();
    }
}
