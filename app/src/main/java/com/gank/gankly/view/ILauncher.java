package com.gank.gankly.view;

import com.gank.gankly.bean.CheckVersion;

/**
 * Create by LingYan on 2016-06-01
 */
public interface ILauncher extends IBaseView {
    void callUpdate(CheckVersion checkVersion);

    void noNewVersion();

    void showDialog();

    void hiddenDialog();
}
