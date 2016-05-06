// Generated code from Butter Knife. Do not modify!
package com.gank.gankly.ui.collect;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CollectAdapter$CollectHolderView$$ViewBinder<T extends com.gank.gankly.ui.collect.CollectAdapter.CollectHolderView> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558524, "field 'title'");
    target.title = finder.castView(view, 2131558524, "field 'title'");
  }

  @Override public void unbind(T target) {
    target.title = null;
  }
}
