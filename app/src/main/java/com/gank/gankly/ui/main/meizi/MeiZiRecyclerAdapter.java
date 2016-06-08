package com.gank.gankly.ui.main.meizi;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.gank.gankly.R;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.listener.MeiziOnClick;
import com.gank.gankly.utils.AppUtils;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-04-06
 */
public class MeiZiRecyclerAdapter extends RecyclerView.Adapter<MeiZiRecyclerAdapter.GoodsViewHolder> {
    private List<ResultsBean> mResults;
    private Activity mContext;
    private LayoutInflater inflater;
    private int mScreenWidth = AppUtils.getDisplayWidth() / 2;
    private Map<String, Integer> widths = new HashMap<>();

    private MeiziOnClick mMeiZiOnClick;

    public class Size {
        int height;
        int width;
    }

    public void setMeiZiOnClick(MeiziOnClick meiZiOnClick) {
        mMeiZiOnClick = meiZiOnClick;
    }

    public MeiZiRecyclerAdapter(Activity context) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        mResults = new ArrayList<>();
    }

    @Override
    public GoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_meizi, parent, false);
        return new GoodsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GoodsViewHolder holder, final int position) {
        final int pos = holder.getLayoutPosition();
        final ResultsBean bean = mResults.get(pos);
//        holder.imgMeizi.setMinimumHeight(500 + (int) (200 * Math.random()));
        final String url = bean.getUrl();
        KLog.d("BitmapImageViewTarget.SIZE_ORIGINAL:" + BitmapImageViewTarget.SIZE_ORIGINAL);
        Glide.with(mContext)
                .load(url)
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .override(BitmapImageViewTarget.SIZE_ORIGINAL, BitmapImageViewTarget.SIZE_ORIGINAL)
                .into(new DriverViewTarget(holder.imgMeizi));

    }

    private class DriverViewTarget extends BitmapImageViewTarget {
        private ImageView image;

        public DriverViewTarget(ImageView view) {
            super(view);
            this.image = view;
        }

        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            int viewWidth = mScreenWidth;
//            float scale = resource.getWidth() / (viewWidth * 1.0f);
//            int viewHeight = (int) (resource.getHeight() * scale);
            KLog.d(" resource.getHeight():" + resource.getHeight() + ",resource.getWidth():" + resource.getWidth());
            int viewHeight = resource.getHeight() * viewWidth / resource.getWidth();
            KLog.d("viewHeight:" + viewHeight);
            setCardViewLayoutParams(viewWidth, viewHeight);
            super.onResourceReady(resource, glideAnimation);
        }

        private void setCardViewLayoutParams(int width, int height) {
            ViewGroup.LayoutParams layoutParams = image.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;
            image.setLayoutParams(layoutParams);
        }
    }


    private int getTargetHeight(int width, int height, String url) {
        int widthTarget;
        if (widths.containsKey(url)) {
            widthTarget = widths.get(url);
        } else {
            widthTarget = mScreenWidth * height / width;
            if (widthTarget > 0) {
                widths.put(url, widthTarget);
            }
        }
        return widthTarget;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        if (mResults != null) {
            return mResults.size();
        }
        return 0;
    }

    public void updateItems(List<ResultsBean> goods) {
        clear();
        addItems(goods);
    }

    public void clear() {
        if (mResults != null) {
            mResults.clear();
        }
    }

    public void addItems(List<ResultsBean> goods) {
        mResults.addAll(goods);
        int position = mResults.size() == 0 ? 0 : mResults.size() - 1;
//        notifyItemRangeInserted(position, goods.size());
        notifyDataSetChanged();
    }

    class GoodsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.meizi_img_picture)
//        RatioImageView imgMeizi;
                ImageView imgMeizi;
        @Bind(R.id.meizi_card_view)
        View mLayout;

        public GoodsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            imgMeizi.setOriginalSize(50, 50);
            imgMeizi.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mMeiZiOnClick != null) {
                mMeiZiOnClick.onClick(v, getLayoutPosition());
            }
        }
    }

}
