// Generated code from Butter Knife. Do not modify!
package com.gank.gankly.ui.main.meizi;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MeiZiRecyclerAdapter$GoodsViewHolder$$ViewBinder<T extends com.gank.gankly.ui.main.meizi.MeiZiRecyclerAdapter.GoodsViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558533, "field 'txtDesc'");
    target.txtDesc = finder.castView(view, 2131558533, "field 'txtDesc'");
    view = finder.findRequiredView(source, 2131558532, "field 'imgMeizi'");
    target.imgMeizi = finder.castView(view, 2131558532, "field 'imgMeizi'");
  }

  @Override public void unbind(T target) {
    target.txtDesc = null;
    target.imgMeizi = null;
  }
}
