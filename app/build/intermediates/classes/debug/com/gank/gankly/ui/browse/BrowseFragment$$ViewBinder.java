// Generated code from Butter Knife. Do not modify!
package com.gank.gankly.ui.browse;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class BrowseFragment$$ViewBinder<T extends com.gank.gankly.ui.browse.BrowseFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558560, "field 'mProgressImageView'");
    target.mProgressImageView = finder.castView(view, 2131558560, "field 'mProgressImageView'");
  }

  @Override public void unbind(T target) {
    target.mProgressImageView = null;
  }
}
