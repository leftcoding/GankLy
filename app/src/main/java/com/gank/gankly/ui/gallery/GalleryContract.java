package com.gank.gankly.ui.gallery;

import android.content.Context;
import android.ly.business.domain.Gank;
import androidx.annotation.NonNull;

import com.gank.gankly.mvp.base.LoadMorePresenter;
import com.gank.gankly.mvp.base.SupportView;

import java.util.List;

/**
 * Create by LingYan on 2017-01-16
 */

public interface GalleryContract {

    interface View extends SupportView {
        void appendData(List<Gank> list);
    }

    abstract class Presenter extends LoadMorePresenter<View> {

        public Presenter(@NonNull Context context, View view) {
            super(context, view);
        }
    }
}
