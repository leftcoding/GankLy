package com.gank.gankly.ui.main.tab;


import android.content.Context;
import androidx.annotation.NonNull;

/**
 * Create by LingYan on 2017-09-28
 */

public class MainTabPresenter extends MainTabContract.Presenter {


    public MainTabPresenter(@NonNull Context context, @NonNull MainTabContract.View view) {
        super(context, view);
    }

    @Override
    protected void onDestroy() {

    }

    @Override
    public void loadPicture() {

    }
}
