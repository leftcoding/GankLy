package com.gank.gankly.ui.ios;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

import static com.gank.gankly.ui.ios.IosAdapter.DefaultItem.VIEW_DEFAULT;

/**
 * Create by LingYan on 2016-04-25
 */
class IosAdapter extends BaseAdapter<IosAdapter.DefaultHolder> {
    private List<ResultsBean> mResults;
    private List<DefaultItem> mItemList;

    private Fragment mFragment;
    private Context mContext;

    private RecyclerOnClick mMeiZiOnClick;

    IosAdapter(Context context) {
        mContext = context;
        setHasStableIds(true);
        mResults = new ArrayList<>();
        mItemList = new ArrayList<>();

        registerAdapterDataObserver(mObserver);
    }

    IosAdapter(@NonNull Fragment fragment) {
        this(fragment.getContext());
        mFragment = fragment;
    }

    private RecyclerView.AdapterDataObserver mObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            mItemList.clear();
            if (mResults != null && !mResults.isEmpty()) {
                for (ResultsBean resultsBean : mResults) {
                    mItemList.add(new TextItem(resultsBean));
                }
            }
        }
    };

    @Override
    public DefaultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DefaultHolder defaultHolder;
        switch (viewType) {
            case VIEW_DEFAULT:
                defaultHolder = new GankViewHolder(parent.getContext(), parent);
                break;
            default:
                defaultHolder = null;
                break;
        }
        return defaultHolder;
    }

    @Override
    public void onBindViewHolder(DefaultHolder holder, int position) {
        holder.bindItem(mFragment, mItemList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return mItemList.get(position).getType();
    }

    @Override
    public void onViewRecycled(DefaultHolder holder) {
        super.onViewRecycled(holder);
        Glide.get(mContext).clearMemory();
    }

    @Override
    public int getItemCount() {
        return mResults == null ? 0 : mResults.size();
    }

    public void fillItems(List<ResultsBean> results) {
        appendItems(results);
    }

    public void appendItems(List<ResultsBean> results) {
        mResults.addAll(results);
        this.notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void destroy() {
        unregisterAdapterDataObserver(mObserver);
    }

    public void setOnItemClickListener(RecyclerOnClick onItemClickListener) {
        mMeiZiOnClick = onItemClickListener;
    }

    class GankViewHolder extends DefaultHolder<TextItem> {
        @BindView(R.id.author_name)
        TextView authorName;

        @BindView(R.id.time)
        TextView time;

        @BindView(R.id.title)
        TextView title;

        GankViewHolder(Context context, ViewGroup parent) {
            super(context, parent, R.layout.adapter_ios);
        }

        @Override
        public void bindItem(Fragment fragment, TextItem item) {
            final Context context = fragment.getContext();
            final ResultsBean resultsBean = item.getResultsBean();
            Date date = DateUtils.formatToDate(resultsBean.publishedAt);
            String formatDate = DateUtils.formatString(date, DateUtils.MM_DD);
            time.setText(formatDate);

            title.setText(resultsBean.desc);
            authorName.setText(resultsBean.getWho());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMeiZiOnClick != null) {
                        mMeiZiOnClick.onClick(v, getAdapterPosition(), resultsBean);
                    }
                }
            });
        }
    }

    abstract class DefaultHolder<II extends DefaultItem> extends ButterHolder {

        DefaultHolder(Context context, ViewGroup parent, @LayoutRes int layoutID) {
            super(context, parent, layoutID);
        }

        public abstract void bindItem(Fragment fragment, II item);
    }

    class TextItem implements DefaultItem {
        private ResultsBean mResultsBean;

        TextItem(ResultsBean resultsBean) {
            mResultsBean = resultsBean;
        }

        ResultsBean getResultsBean() {
            return mResultsBean;
        }

        @Override
        public int getType() {
            return VIEW_DEFAULT;
        }
    }

    interface DefaultItem {
        int VIEW_DEFAULT = 1;

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
