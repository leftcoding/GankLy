// Generated code from Butter Knife. Do not modify!
package com.gank.gankly.ui.main;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class GankAdapter$GankViewHolder$$ViewBinder<T extends com.gank.gankly.ui.main.GankAdapter.GankViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558535, "field 'txtDesc'");
    target.txtDesc = finder.castView(view, 2131558535, "field 'txtDesc'");
    view = finder.findRequiredView(source, 2131558540, "field 'txtName'");
    target.txtName = finder.castView(view, 2131558540, "field 'txtName'");
    view = finder.findRequiredView(source, 2131558538, "field 'txtTime'");
    target.txtTime = finder.castView(view, 2131558538, "field 'txtTime'");
    view = finder.findRequiredView(source, 2131558539, "field 'txtFrom'");
    target.txtFrom = finder.castView(view, 2131558539, "field 'txtFrom'");
    view = finder.findRequiredView(source, 2131558537, "field 'img'");
    target.img = finder.castView(view, 2131558537, "field 'img'");
  }

  @Override public void unbind(T target) {
    target.txtDesc = null;
    target.txtName = null;
    target.txtTime = null;
    target.txtFrom = null;
    target.img = null;
  }
}
