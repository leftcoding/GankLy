package com.gank.gankly.ui.base;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.butterknife.ButterKnifeFragment;
import com.gank.gankly.rxjava.RxBus_;
import com.gank.gankly.rxjava.theme.ThemeEvent;
import com.gank.gankly.utils.ListUtils;
import com.gank.gankly.widget.LYRelativeLayoutRipple;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2016-09-13
 */
public abstract class BaseThemeFragment extends ButterKnifeFragment {
    @NonNull
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Disposable mDisposable;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDisposable = RxBus_.getInstance().toObservable(ThemeEvent.class)
                .subscribe(themeEvent -> callBackRefreshUi());
    }

    protected abstract void callBackRefreshUi();

//    @Override
//    public void changeThemes() {
//        super.changeThemes();
//        if (mSwipeRefreshLayout != null) {
//            StyleUtils.changeSwipeRefreshLayout(mSwipeRefreshLayout);
//        }
//    }

    public void setSwipeRefreshLayout(@NonNull SwipeRefreshLayout swipeRefreshLayout) {
        this.mSwipeRefreshLayout = swipeRefreshLayout;
    }

    public void setItemSelectBackground(@NonNull List<View> list) {
        if (!ListUtils.isEmpty(list)) {
            int[] attrs = new int[]{R.attr.selectableItemBackground};
            TypedArray typedArray = getActivity().obtainStyledAttributes(attrs);
            final int backgroundResource = typedArray.getResourceId(0, 0);

//            ButterKnife.apply(list, new ButterKnife.Action<View>() {
//                @Override
//                public void apply(@NonNull View view, int index) {
//                    view.setBackgroundResource(backgroundResource);
//                }
//            });
            typedArray.recycle();
        }
    }

    public void setItemBackground(@NonNull List<LYRelativeLayoutRipple> list) {
        if (!ListUtils.isEmpty(list)) {
            final int backgroundResource = R.attr.lyItemSelectBackground;
//            ButterKnife.apply(list, new ButterKnife.Action<LYRelativeLayoutRipple>() {
//                @Override
//                public void apply(@NonNull LYRelativeLayoutRipple view, int index) {
//                    view.setCustomBackgroundResource(backgroundResource);
//                }
//            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
