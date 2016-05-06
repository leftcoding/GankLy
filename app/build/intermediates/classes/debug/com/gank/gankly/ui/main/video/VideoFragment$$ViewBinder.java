// Generated code from Butter Knife. Do not modify!
package com.gank.gankly.ui.main.video;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class VideoFragment$$ViewBinder<T extends com.gank.gankly.ui.main.video.VideoFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558568, "field 'mCoordinatorLayout'");
    target.mCoordinatorLayout = finder.castView(view, 2131558568, "field 'mCoordinatorLayout'");
    view = finder.findRequiredView(source, 2131558510, "field 'mToolbar'");
    target.mToolbar = finder.castView(view, 2131558510, "field 'mToolbar'");
    view = finder.findRequiredView(source, 2131558570, "field 'mRecyclerView'");
    target.mRecyclerView = finder.castView(view, 2131558570, "field 'mRecyclerView'");
    view = finder.findRequiredView(source, 2131558569, "field 'mSwipeRefresh'");
    target.mSwipeRefresh = finder.castView(view, 2131558569, "field 'mSwipeRefresh'");
  }

  @Override public void unbind(T target) {
    target.mCoordinatorLayout = null;
    target.mToolbar = null;
    target.mRecyclerView = null;
    target.mSwipeRefresh = null;
  }
}
