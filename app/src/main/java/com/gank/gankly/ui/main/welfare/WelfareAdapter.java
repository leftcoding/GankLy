package com.gank.gankly.ui.main.welfare;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.gank.gankly.R;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.listener.MeiziOnClick;
import com.gank.gankly.utils.AppUtils;
import com.gank.gankly.utils.gilde.ImageLoaderUtil;
import com.gank.gankly.widget.ImageDefaultView;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-04-06
 * Email:137387869@qq.com
 */
public class WelfareAdapter extends RecyclerView.Adapter<WelfareAdapter.GoodsViewHolder> {
    private List<ResultsBean> mResults;
    private Activity mActivity;
    private LayoutInflater inflater;
    private int mScreenWidth = AppUtils.getDisplayWidth() / 2;
    private int mScreenHeight = AppUtils.getDisplayWidth() / 2;
    private ArrayMap<String, Integer> heights = new ArrayMap<>();

    private MeiziOnClick mMeiZiOnClick;
    private Context mContext;

    public void setMeiZiOnClick(MeiziOnClick meiZiOnClick) {
        mMeiZiOnClick = meiZiOnClick;
    }

    public WelfareAdapter(Activity activity) {
        setHasStableIds(true);
        inflater = LayoutInflater.from(activity);
        mActivity = activity;
        mResults = new ArrayList<>();
    }

    @Override
    public GoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = inflater.inflate(R.layout.adapter_meizi, parent, false);
        return new GoodsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GoodsViewHolder holder, int position) {
        final ResultsBean bean = mResults.get(position);
        final String url = bean.getUrl();
        BitmapRequestBuilder<String, Bitmap> requestBuilder = ImageLoaderUtil.getInstance()
                .glideAsBitmap(mContext, url);
        requestBuilder.listener(new RequestListener<String, Bitmap>() {
            @Override
            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                KLog.e("onException model:" + model);
                holder.imgMeizi.showLoadText();
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                holder.imgMeizi.showImage();
                bean.setLoad(true);
                mResults.set(position, bean);
                return false;
            }
        })
                .fitCenter()
                .into(holder.mImageView);

        if (heights.containsKey(url)) {
            setCardViewLayoutParams(holder.imgMeizi, mScreenWidth, heights.get(url));
            requestBuilder.into(holder.mImageView);
        } else {
            requestBuilder.override(mScreenWidth, mScreenHeight);//设置宽高一致，后期改动不大
            requestBuilder.into(new DriverViewTarget(holder.mImageView, url, holder.imgMeizi));
        }

        holder.imgMeizi.setOnClickListener(v -> {
            if (bean.isLoad()) {
                mMeiZiOnClick.onClick(v, position);
            } else {
                if (!holder.imgMeizi.isCanLoad()) {
                    return;
                }
                holder.imgMeizi.showLoading();
                ImageLoaderUtil.getInstance().loadAsImage(mContext, url).listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        holder.imgMeizi.showErrorText();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.imgMeizi.showImage();
                        bean.setLoad(true);
                        mResults.set(position, bean);
                        return false;
                    }
                })
                        .fitCenter()
                        .into(new DriverViewTarget(holder.mImageView, url, holder.imgMeizi));
            }
        });


    }

    @Override
    public void onViewRecycled(GoodsViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.clear(holder.mImageView);//view recycled,clear image request
        holder.mImageView.setImageBitmap(null);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class DriverViewTarget extends BitmapImageViewTarget {
        private ImageView mImageView;
        private String url;
        private ImageDefaultView defaultView;

        public DriverViewTarget(ImageView image, String url, ImageDefaultView defaultView) {
            super(image);
            this.mImageView = image;
            this.url = url;
            this.defaultView = defaultView;
        }

        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            int viewWidth = mScreenWidth;
            int viewHeight;
            if (!heights.containsKey(url) && url != null) {
                viewHeight = resource.getHeight() * viewWidth / resource.getWidth();
                heights.put(url, viewHeight);
                setCardViewLayoutParams(defaultView, viewWidth, viewHeight);
            }
            super.onResourceReady(resource, glideAnimation);
        }
    }

    private void setCardViewLayoutParams(ImageDefaultView mImageView, int width, int height) {
        ViewGroup.LayoutParams layoutParams = mImageView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        mImageView.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    public void refillItems(List<ResultsBean> goods) {
        clear();
        appendItems(goods);
    }

    public void clear() {
        mResults.clear();
        heights.clear();
        int size = mResults.size();
        notifyItemRangeRemoved(0, size);
    }

    public void appendItems(List<ResultsBean> goods) {
        mResults.addAll(goods);
        notifyItemRangeInserted(mResults.size(), goods.size());
    }

    class GoodsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.meizi_img_picture)
        ImageDefaultView imgMeizi;
        @BindView(R.id.meizi_card_view)
        RelativeLayout mRelativeLayout;
        ImageView mImageView;

        public GoodsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mImageView = new ImageView(mContext);
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imgMeizi.setFrameLayout(mImageView);
        }
    }
}
