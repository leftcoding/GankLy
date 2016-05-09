// Generated code from Butter Knife. Do not modify!
package com.gank.gankly.widget;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class DeleteDialog$$ViewBinder<T extends com.gank.gankly.widget.DeleteDialog> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558557, "field 'txtContent'");
    target.txtContent = finder.castView(view, 2131558557, "field 'txtContent'");
    view = finder.findRequiredView(source, 2131558558, "method 'onNavigation'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onNavigation();
        }
      });
    view = finder.findRequiredView(source, 2131558559, "method 'onCancel'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onCancel();
        }
      });
  }

  @Override public void unbind(T target) {
    target.txtContent = null;
  }
}
