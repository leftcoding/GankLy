// Generated code from Butter Knife. Do not modify!
package com.gank.gankly.ui.main;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MainActivity$$ViewBinder<T extends com.gank.gankly.ui.main.MainActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558519, "field 'mNavigationView'");
    target.mNavigationView = finder.castView(view, 2131558519, "field 'mNavigationView'");
    view = finder.findRequiredView(source, 2131558517, "field 'mDrawerLayout'");
    target.mDrawerLayout = finder.castView(view, 2131558517, "field 'mDrawerLayout'");
  }

  @Override public void unbind(T target) {
    target.mNavigationView = null;
    target.mDrawerLayout = null;
  }
}
