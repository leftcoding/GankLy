package com.gank.gankly.ui.base;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.utils.ListUtils;
import com.gank.gankly.utils.StyleUtils;
import com.gank.gankly.widget.LYRelativeLayoutRipple;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-09-13
 * Email:137387869@qq.com
 */
public abstract class BaseThemeFragment extends BaseFragment {
    @NonNull
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void changeThemes() {
        super.changeThemes();
        if (mSwipeRefreshLayout != null) {
            StyleUtils.changeSwipeRefreshLayout(mSwipeRefreshLayout);
        }
    }

    public void setSwipeRefreshLayout(@NonNull SwipeRefreshLayout swipeRefreshLayout) {
        if (swipeRefreshLayout == null) {
            throw new NullPointerException("SwipeRefreshLayout can't be null");
        }
        this.mSwipeRefreshLayout = swipeRefreshLayout;
    }

    public void setItemSelectBackground(@NonNull List<View> list) {
        if (!ListUtils.isListEmpty(list)) {
            int[] attrs = new int[]{R.attr.selectableItemBackground};
            TypedArray typedArray = getActivity().obtainStyledAttributes(attrs);
            final int backgroundResource = typedArray.getResourceId(0, 0);

            ButterKnife.apply(list, new ButterKnife.Action<View>() {
                @Override
                public void apply(@NonNull View view, int index) {
                    view.setBackgroundResource(backgroundResource);
                }
            });
        }
    }

    public void setItemBackground(@NonNull List<LYRelativeLayoutRipple> list) {
        if (!ListUtils.isListEmpty(list)) {
            final int backgroundResource = R.attr.lyItemSelectBackground;
            ButterKnife.apply(list, new ButterKnife.Action<LYRelativeLayoutRipple>() {
                @Override
                public void apply(@NonNull LYRelativeLayoutRipple view, int index) {
                    view.setCustomBackgroundResource(backgroundResource);
                }
            });
        }
    }
}
