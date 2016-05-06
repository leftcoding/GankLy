// Generated code from Butter Knife. Do not modify!
package com.gank.gankly.ui.collect;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CollectActivity$$ViewBinder<T extends com.gank.gankly.ui.collect.CollectActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558513, "field 'mToolbar'");
    target.mToolbar = finder.castView(view, 2131558513, "field 'mToolbar'");
    view = finder.findRequiredView(source, 2131558515, "field 'mRecyclerView'");
    target.mRecyclerView = finder.castView(view, 2131558515, "field 'mRecyclerView'");
    view = finder.findRequiredView(source, 2131558514, "field 'mSwipeRefreshLayout'");
    target.mSwipeRefreshLayout = finder.castView(view, 2131558514, "field 'mSwipeRefreshLayout'");
    view = finder.findRequiredView(source, 2131558516, "field 'mLoadingView'");
    target.mLoadingView = view;
    view = finder.findRequiredView(source, 2131558511, "field 'mMain'");
    target.mMain = view;
  }

  @Override public void unbind(T target) {
    target.mToolbar = null;
    target.mRecyclerView = null;
    target.mSwipeRefreshLayout = null;
    target.mLoadingView = null;
    target.mMain = null;
  }
}
