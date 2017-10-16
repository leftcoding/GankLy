package com.gank.gankly.view;

import com.gank.gankly.bean.CheckVersion;
import com.gank.gankly.mvp.base.BaseView;

/**
 * Create by LingYan on 2016-06-01
 */
public interface ILauncher extends BaseView {
    void callUpdate(CheckVersion checkVersion);

    void showDialog();

    void noNewVersion();

    void hiddenDialog();
}
