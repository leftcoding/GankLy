package com.gank.gankly.ui.baisi.image;

import android.app.Activity;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gank.gankly.R;
import com.gank.gankly.bean.BuDeJieBean;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-04-11
 */
public class BaiSiImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String IMAGE_JPG = ".jpg";
    private static final String IMAGE_PNG = ".png";
    private static final String IMAGE_GIF = ".gif";
    private Map<String, Integer> maps = new ArrayMap<>();

    //    private List<BaiSiBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean> mList;
    private List<BuDeJieBean.ListBean> mList;
    private final Activity mContext;
    private onPlayClick playclick;

    public BaiSiImageAdapter(Activity context) {
        this.mContext = context;
        mList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = View.inflate(mContext, R.layout.adapter_baisi_image_gif, null);
            return new BaiSiImageView(view);
        } else {
            view = View.inflate(mContext, R.layout.adapter_baisi_image, null);
            return new BaiSiSecondImageView(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BuDeJieBean.ListBean bean = mList.get(position);
        String imgUrl;
        if (bean.getGif() != null) {
            imgUrl = bean.getGif().getImages().get(0);
        } else {
            imgUrl = bean.getImage().getBig().get(0);
        }

        if (holder instanceof BaiSiImageView) {
            BaiSiImageView baiSiHolderView = (BaiSiImageView) holder;
            baiSiHolderView.update(position, imgUrl);
            baiSiHolderView.name.setText(bean.getU().getName());
            baiSiHolderView.title.setText(bean.getText());
            String passtime = bean.getPasstime();
            if (!passtime.startsWith("2")) {
                int index = passtime.indexOf("2");
                if (index > 0) {
                    passtime = passtime.substring(0, index + 1);
                }
            }

            baiSiHolderView.mTime.setText(passtime);
            Glide.with(mContext).load(bean.getU().getHeader().get(0)).into(baiSiHolderView.imgPortarit);

            Glide.with(mContext)
                    .load(imgUrl)
//                    .centerCrop()
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // gif must be add this
//                    .dontAnimate()
//                    .skipMemoryCache(true)
                    .into(baiSiHolderView.mGif);

//            int height;
//            if (!maps.containsKey(imgUrl)) {
//                KLog.d(bean.getGif().getHeight() + ":" + bean.getGif().getWidth() + ",imgUrl:" + imgUrl);
//                height = bean.getGif().getHeight() * 1080 / bean.getGif().getWidth();
//                KLog.d("height:" + height);
//                maps.put(imgUrl, height);
//            } else {
//                height = maps.get(imgUrl);
//            }
//            ViewGroup.LayoutParams layoutParams = baiSiHolderView.mGif.getLayoutParams();
//            layoutParams.width = 1080;
//            layoutParams.height = height;
//            baiSiHolderView.mGif.setLayoutParams(layoutParams);

        } else if (holder instanceof BaiSiSecondImageView) {
            BaiSiSecondImageView baiSiSecondImageView = (BaiSiSecondImageView) holder;
            baiSiSecondImageView.update(position, imgUrl);
            baiSiSecondImageView.name.setText(bean.getU().getName());
            baiSiSecondImageView.title.setText(bean.getText());
            String passtime = bean.getPasstime();
            if (!passtime.startsWith("2")) {
                int index = passtime.indexOf("2");
                if (index > 0) {
                    passtime = passtime.substring(0, index + 1);
                }
            }

            baiSiSecondImageView.mTime.setText(passtime);
            Glide.with(mContext).load(bean.getU().getHeader().get(0)).into(baiSiSecondImageView.imgPortarit);

            if (!imgUrl.endsWith(IMAGE_GIF)) {
                final String imgUrL = bean.getImage().getThumbnail_small().get(0);
                Glide.with(mContext)
                        .load(imgUrL)
                        .centerCrop()
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .crossFade()
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                KLog.d("model:" + model);
                                return false;
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(baiSiSecondImageView.mPicture);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getGif() != null ? 1 : 2;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class BaiSiImageView extends RecyclerView.ViewHolder {
        @BindView(R.id.adapter_player_control)
        RelativeLayout rlayPlayerControl;
        @BindView(R.id.baisi_txt_name)
        TextView name;
        @BindView(R.id.baisi_video_title)
        TextView title;
        @BindView(R.id.baisi_img_portrait)
        ImageView imgPortarit;
        @BindView(R.id.baisi_txt_time)
        TextView mTime;
        @BindView(R.id.baisi_gallery_img_gif)
        ImageView mGif;

        public BaiSiImageView(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void update(final int position, final String url) {
            //点击回调 播放视频
            rlayPlayerControl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (playclick != null) {
                        KLog.d("url:" + url);
                        playclick.onPlayclick(position, rlayPlayerControl, url);
                    }
                }
            });
        }
    }


    public class BaiSiSecondImageView extends RecyclerView.ViewHolder {
        @BindView(R.id.adapter_player_control)
        RelativeLayout rlayPlayerControl;
        @BindView(R.id.baisi_txt_name)
        TextView name;
        @BindView(R.id.baisi_video_title)
        TextView title;
        @BindView(R.id.baisi_img_portrait)
        ImageView imgPortarit;
        @BindView(R.id.baisi_txt_time)
        TextView mTime;
        @BindView(R.id.baisi_img_pic)
        ImageView mPicture;

        public BaiSiSecondImageView(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void update(final int position, final String url) {
            //点击回调 播放视频
            rlayPlayerControl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (playclick != null) {
                        KLog.d("url:" + url);
                        playclick.onPlayclick(position, rlayPlayerControl, url);
                    }
                }
            });
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        if (holder instanceof BaiSiImageView) {
            BaiSiImageView baiSiImageView = (BaiSiImageView) holder;
            Glide.clear(baiSiImageView.mGif);
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

    public void setPlayClick(onPlayClick playclick) {
        this.playclick = playclick;
    }

    public interface onPlayClick {
        void onPlayclick(int position, View image, String url);
    }
}
