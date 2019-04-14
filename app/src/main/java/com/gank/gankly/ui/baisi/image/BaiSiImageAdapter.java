package com.gank.gankly.ui.baisi.image;

import android.content.Context;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gank.gankly.R;
import com.gank.gankly.bean.BuDeJieBean;
import com.gank.gankly.bean.GallerySize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-04-11
 */
public class BaiSiImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Map<String, Integer> mGifHeight = new ArrayMap<>();
    private Map<String, Integer> mImageHeight = new ArrayMap<>();
    private List<BuDeJieBean.ListBean> mList;
    private Context mContext;
    private onClickImage mOnClickImage;

    public BaiSiImageAdapter(Context context) {
        setHasStableIds(true);
        mContext = context;
        mList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = View.inflate(mContext, R.layout.adapter_baisi_image_gif, null);
            return new BaiSiGifHolder(view);
        } else {
            view = View.inflate(mContext, R.layout.adapter_baisi_image, null);
            return new BaiSiNormalImageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String imgUrl;
        int height;
        int mPreHeight;
        int mPreWidth;

        BuDeJieBean.ListBean bean = mList.get(position);
        String passtime = bean.getPasstime();
        String u_name = bean.getU().getName();
        String text = bean.getText();

        if (!passtime.startsWith("2")) {
            int index = passtime.indexOf("2");
            if (index > 0) {
                passtime = passtime.substring(index, passtime.length());
            }
        }
        if (bean.getGif() != null) {
            BuDeJieBean.ListBean.GifBean gif = bean.getGif();
            imgUrl = gif.getImages().get(0);
            mPreHeight = gif.getHeight();
            mPreWidth = gif.getWidth();

            if (!mGifHeight.containsKey(imgUrl)) {
                height = mPreHeight * 1080 / mPreWidth;
                mGifHeight.put(imgUrl, height);
            } else {
                height = mGifHeight.get(imgUrl);
            }
        } else {
            BuDeJieBean.ListBean.ImageBean imageBean = bean.getImage();
            imgUrl = imageBean.getDownload_url().get(3);
            mPreHeight = imageBean.getHeight();
            mPreWidth = imageBean.getWidth();

            if (!mImageHeight.containsKey(imgUrl)) {
                height = mPreHeight * 1080 / mPreWidth;
                height = height > 630 ? 630 : height;
                mImageHeight.put(imgUrl, height);
            } else {
                height = mImageHeight.get(imgUrl);
            }
        }

        if (holder instanceof BaiSiGifHolder) {
            BaiSiGifHolder gifHolder = (BaiSiGifHolder) holder;
            gifHolder.update(position, imgUrl);

            gifHolder.mName.setText(u_name);
            gifHolder.mTitle.setText(text);

            gifHolder.height = mPreHeight;
            gifHolder.width = mPreWidth;
            gifHolder.mTime.setText(passtime);
            Glide.with(mContext).load(bean.getU().getHeader().get(0)).into(gifHolder.imgPortarit);

            Glide.with(mContext)
                    .load(imgUrl)
                    .apply(new RequestOptions()
                            .priority(Priority.IMMEDIATE)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                    )
                    .into(gifHolder.mGif);

            ViewGroup.LayoutParams layoutParams = gifHolder.rlayPlayerControl.getLayoutParams();
            layoutParams.width = 1080;
//            layoutParams.height = height;
            layoutParams.height = 630;
            gifHolder.rlayPlayerControl.setLayoutParams(layoutParams);

        } else if (holder instanceof BaiSiNormalImageHolder) {
            BaiSiNormalImageHolder imageHolder = (BaiSiNormalImageHolder) holder;
            imageHolder.update(position, imgUrl);
            imageHolder.mName.setText(u_name);
            imageHolder.mTitle.setText(text);

            imageHolder.height = mPreHeight;
            imageHolder.width = mPreWidth;
            imageHolder.mTime.setText(passtime);
            Glide.with(mContext).load(bean.getU().getHeader().get(0)).into(imageHolder.imgPortarit);
            Glide.with(mContext)
                    .load(imgUrl)
                    .apply(new RequestOptions()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                    )
                    .into(imageHolder.mPicture);

            ViewGroup.LayoutParams layoutParams = imageHolder.rlayPlayerControl.getLayoutParams();
            layoutParams.width = 1080;
            layoutParams.height = height;
            imageHolder.rlayPlayerControl.setLayoutParams(layoutParams);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getGif() != null ? 1 : 2;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class BaiSiGifHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.baisi_player_control)
        RelativeLayout rlayPlayerControl;
        @BindView(R.id.baisi_txt_name)
        TextView mName;
        @BindView(R.id.baisi_video_title)
        TextView mTitle;
        @BindView(R.id.baisi_img_portrait)
        ImageView imgPortarit;
        @BindView(R.id.baisi_txt_time)
        TextView mTime;
        @BindView(R.id.baisi_gallery_img_gif)
        ImageView mGif;
        int width;
        int height;

        public BaiSiGifHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void update(final int position, final String url) {
            rlayPlayerControl.setOnClickListener(v -> {
                if (mOnClickImage != null) {
                    mOnClickImage.onClick(new GallerySize(height, width, url, position));
                }
            });
        }
    }

    public class BaiSiNormalImageHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.baisi_player_control)
        RelativeLayout rlayPlayerControl;
        @BindView(R.id.baisi_txt_name)
        TextView mName;
        @BindView(R.id.baisi_video_title)
        TextView mTitle;
        @BindView(R.id.baisi_img_portrait)
        ImageView imgPortarit;
        @BindView(R.id.baisi_txt_time)
        TextView mTime;
        @BindView(R.id.baisi_img_pic)
        ImageView mPicture;
        int height;
        int width;

        public BaiSiNormalImageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void update(final int position, final String url) {
            //点击回调 播放视频
            rlayPlayerControl.setOnClickListener(v -> {
                if (mOnClickImage != null) {
                    mOnClickImage.onClick(new GallerySize(height, width, url, position));
                }
            });
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        if (holder instanceof BaiSiGifHolder) {
            Glide.get(mContext).clearMemory();
        } else if (holder instanceof BaiSiNormalImageHolder) {
//            BaiSiNormalImageHolder baiSiGifHolder = (BaiSiNormalImageHolder) holder;
//            Glide.clear(baiSiGifHolder.mPicture);
            Glide.get(mContext).clearMemory();
        }
        super.onViewRecycled(holder);
    }

    public void refillItems(List<BuDeJieBean.ListBean> list) {
        int size = mList.size();
        mList.clear();
        notifyItemRangeRemoved(0, size);
        appendItems(list);
    }

    public void appendItems(List<BuDeJieBean.ListBean> list) {
        int size = mList.size();
        mList.addAll(list);
        notifyItemRangeInserted(size, list.size());
    }

    public void setPlayClick(onClickImage playclick) {
        this.mOnClickImage = playclick;
    }

    public interface onClickImage {
        void onClick(GallerySize gallerySize);
    }
}
