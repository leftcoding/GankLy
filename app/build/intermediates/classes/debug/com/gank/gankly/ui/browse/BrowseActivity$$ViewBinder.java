// Generated code from Butter Knife. Do not modify!
package com.gank.gankly.ui.browse;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class BrowseActivity$$ViewBinder<T extends com.gank.gankly.ui.browse.BrowseActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558510, "field 'mToolbar'");
    target.mToolbar = finder.castView(view, 2131558510, "field 'mToolbar'");
    view = finder.findRequiredView(source, 2131558509, "field 'mViewPager'");
    target.mViewPager = finder.castView(view, 2131558509, "field 'mViewPager'");
  }

  @Override public void unbind(T target) {
    target.mToolbar = null;
    target.mViewPager = null;
  }
}
