// Generated code from Butter Knife. Do not modify!
package com.gank.gankly.ui.web;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class WebActivity$$ViewBinder<T extends com.gank.gankly.ui.web.WebActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558521, "field 'mWebView'");
    target.mWebView = finder.castView(view, 2131558521, "field 'mWebView'");
    view = finder.findRequiredView(source, 2131558510, "field 'mToolbar'");
    target.mToolbar = finder.castView(view, 2131558510, "field 'mToolbar'");
    view = finder.findRequiredView(source, 2131558522, "field 'mProgressBar'");
    target.mProgressBar = finder.castView(view, 2131558522, "field 'mProgressBar'");
  }

  @Override public void unbind(T target) {
    target.mWebView = null;
    target.mToolbar = null;
    target.mProgressBar = null;
  }
}
