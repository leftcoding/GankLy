// Generated code from Butter Knife. Do not modify!
package com.gank.gankly.ui.about;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AboutActivity$$ViewBinder<T extends com.gank.gankly.ui.about.AboutActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558510, "field 'mToolbar'");
    target.mToolbar = finder.castView(view, 2131558510, "field 'mToolbar'");
    view = finder.findRequiredView(source, 2131558508, "field 'mRotateLoading'");
    target.mRotateLoading = finder.castView(view, 2131558508, "field 'mRotateLoading'");
  }

  @Override public void unbind(T target) {
    target.mToolbar = null;
    target.mRotateLoading = null;
  }
}
