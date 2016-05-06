// Generated code from Butter Knife. Do not modify!
package com.gank.gankly.ui.main;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MainFragment$$ViewBinder<T extends com.gank.gankly.ui.main.MainFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558513, "field 'mToolbar'");
    target.mToolbar = finder.castView(view, 2131558513, "field 'mToolbar'");
    view = finder.findRequiredView(source, 2131558575, "field 'mTabLayout'");
    target.mTabLayout = finder.castView(view, 2131558575, "field 'mTabLayout'");
    view = finder.findRequiredView(source, 2131558576, "field 'mViewPager'");
    target.mViewPager = finder.castView(view, 2131558576, "field 'mViewPager'");
  }

  @Override public void unbind(T target) {
    target.mToolbar = null;
    target.mTabLayout = null;
    target.mViewPager = null;
  }
}
