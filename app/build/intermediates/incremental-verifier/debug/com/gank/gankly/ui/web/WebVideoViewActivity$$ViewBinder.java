// Generated code from Butter Knife. Do not modify!
package com.gank.gankly.ui.web;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class WebVideoViewActivity$$ViewBinder<T extends com.gank.gankly.ui.web.WebVideoViewActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558571, "field 'mLoveVideoView'");
    target.mLoveVideoView = finder.castView(view, 2131558571, "field 'mLoveVideoView'");
  }

  @Override public void unbind(T target) {
    target.mLoveVideoView = null;
  }
}
