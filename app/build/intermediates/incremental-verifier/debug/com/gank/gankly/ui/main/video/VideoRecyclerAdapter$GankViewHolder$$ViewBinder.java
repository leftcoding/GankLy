// Generated code from Butter Knife. Do not modify!
package com.gank.gankly.ui.main.video;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class VideoRecyclerAdapter$GankViewHolder$$ViewBinder<T extends com.gank.gankly.ui.main.video.VideoRecyclerAdapter.GankViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558535, "field 'txtDesc'");
    target.txtDesc = finder.castView(view, 2131558535, "field 'txtDesc'");
    view = finder.findRequiredView(source, 2131558536, "field 'txtTime'");
    target.txtTime = finder.castView(view, 2131558536, "field 'txtTime'");
    view = finder.findRequiredView(source, 2131558534, "field 'mImageView'");
    target.mImageView = finder.castView(view, 2131558534, "field 'mImageView'");
  }

  @Override public void unbind(T target) {
    target.txtDesc = null;
    target.txtTime = null;
    target.mImageView = null;
  }
}
