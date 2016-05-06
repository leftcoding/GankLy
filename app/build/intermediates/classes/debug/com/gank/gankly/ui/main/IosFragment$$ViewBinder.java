// Generated code from Butter Knife. Do not modify!
package com.gank.gankly.ui.main;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class IosFragment$$ViewBinder<T extends com.gank.gankly.ui.main.IosFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558515, "field 'mRecyclerView'");
    target.mRecyclerView = finder.castView(view, 2131558515, "field 'mRecyclerView'");
    view = finder.findRequiredView(source, 2131558514, "field 'mSwipeRefreshLayout'");
    target.mSwipeRefreshLayout = finder.castView(view, 2131558514, "field 'mSwipeRefreshLayout'");
  }

  @Override public void unbind(T target) {
    target.mRecyclerView = null;
    target.mSwipeRefreshLayout = null;
  }
}
