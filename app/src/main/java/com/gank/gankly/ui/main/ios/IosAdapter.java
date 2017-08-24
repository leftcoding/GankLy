package com.gank.gankly.ui.main.ios;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.gank.gankly.R;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.databinding.AdapterIosBinding;
import com.gank.gankly.listener.RecyclerOnClick;
import com.gank.gankly.utils.DateUtils;
import com.gank.gankly.utils.gilde.ImageLoaderUtil;
import com.gank.gankly.widget.ImageDefaultView;
import com.gank.gankly.widget.RatioImageView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-04-25
 * Email:137387869@qq.com
 */
public class IosAdapter extends RecyclerView.Adapter<IosAdapter.GankViewHolder> {
    private final List<ResultsBean> mResults;
    private RecyclerOnClick mMeiZiOnClick;
    private Context mContext;

    private int mImageSize;
    private List<ResultsBean> mImagesList;

    public IosAdapter(Context context) {
        setHasStableIds(true);
        mResults = new ArrayList<>();
        mContext = context;
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
        holder.bind(bean);

        int imgPosition = position;

        if (mImageSize != 0) {
            if (imgPosition > mImageSize) {
                imgPosition = position % mImageSize;
            } else if (position == mImageSize) {
                imgPosition = 0;
            }
        }

        String url = mImagesList.get(imgPosition).getUrl();
        if (imgPosition < mImageSize) {
            ImageLoaderUtil.getInstance()
                    .loadWifiImage(mContext, url)
                    .apply(new RequestOptions()
                            .centerCrop()
                    )
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.imgHead.showLoadText();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            bean.setLoad(true);
                            mResults.set(position, bean);
                            holder.imgHead.showImage();
                            return false;
                        }
                    })
                    .into(holder.mImageView);
        }

        holder.imgHead.setOnClickListener(v -> {
            if (bean.isLoad()) {
                mMeiZiOnClick.onClick(v, bean);
            } else {
                if (!holder.imgHead.isCanLoad()) {
                    return;
                }
                holder.imgHead.showLoading();
                ImageLoaderUtil.getInstance()
                        .loadManualImage(mContext, url)
                        .apply(new RequestOptions()
                                .centerCrop()
                        )
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                holder.imgHead.showErrorText();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.imgHead.showImage();
                                bean.setLoad(true);
                                mResults.set(position, bean);
                                return false;
                            }
                        })
                        .into(holder.mImageView);
            }
        });
    }

    @Override
    public void onViewRecycled(GankViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.get(mContext).clearMemory();
    }

    private View getLayoutView(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return layoutInflater.inflate(R.layout.adapter_ios, parent, false);
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    public void refillItems(List<ResultsBean> results) {
        shuffleImages();

        int size = mResults.size();
        mResults.clear();
        notifyItemRangeRemoved(0, size);
        appendItems(results);
    }

    public void appendItems(List<ResultsBean> results) {
        mResults.addAll(results);
        int size = mResults.size();
        notifyItemRangeInserted(size, results.size());
    }

    private void shuffleImages() {
        List<ResultsBean> list = MeiziArrayList.getInstance().getOneItemsList();
        mImagesList = new ArrayList<>(list);
        mImageSize = mImagesList.size();
//        Collections.shuffle(mImagesList);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setOnItemClickListener(RecyclerOnClick onItemClickListener) {
        mMeiZiOnClick = onItemClickListener;
    }

    public class GankViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.ios_ratio_img_head)
        ImageDefaultView imgHead;
        RatioImageView mImageView;


        private ResultsBean mBean;
        private AdapterIosBinding mIosBinding;

        public GankViewHolder(View itemView) {
            super(itemView);
            bindView(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            mImageView = new RatioImageView(mContext);
            imgHead.setFrameLayout(mImageView);
        }

        public void bind(ResultsBean resultsBean) {
            Date date = DateUtils.formatDateFromStr(resultsBean.getPublishedAt());
            String formatDate = DateUtils.getFormatDate(date, DateUtils.MM_DD);
            resultsBean.setPublishedAt(formatDate);
            mIosBinding.setResult(resultsBean);
        }

        @Override
        public void onClick(View v) {
            if (mMeiZiOnClick != null) {
                mMeiZiOnClick.onClick(v, mBean);
            }
        }

        private void bindView(View itemView) {
            mIosBinding = DataBindingUtil.bind(itemView);
        }
    }
}
