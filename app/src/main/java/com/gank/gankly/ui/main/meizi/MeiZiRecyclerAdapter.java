package com.gank.gankly.ui.main.meizi;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.gank.gankly.R;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.listener.MeiziOnClick;
import com.gank.gankly.utils.AppUtils;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

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

    private MeiziOnClick mMeiZiOnClick;

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

        final ResultsBean bean = mResults.get(position);
        final String url = bean.getUrl();

        Glide.with(mContext)
                .load(url)
                .asBitmap()
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .override(BitmapImageViewTarget.SIZE_ORIGINAL, BitmapImageViewTarget.SIZE_ORIGINAL)
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
//            KLog.d(" resource.getHeight():" + resource.getHeight() + ",resource.getWidth():" + resource.getWidth());
            int viewHeight = resource.getHeight() * viewWidth / resource.getWidth();
//            KLog.d("viewHeight:" + viewHeight);
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
        notifyItemRangeInserted(position, goods.size());
    }

    class GoodsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.meizi_img_picture)
        ImageView imgMeizi;

        public GoodsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imgMeizi.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mMeiZiOnClick != null) {
                mMeiZiOnClick.onClick(v, getAdapterPosition());
            }
        }
    }

}
