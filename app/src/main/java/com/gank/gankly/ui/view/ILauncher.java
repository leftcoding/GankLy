package com.gank.gankly.ui.view;

import com.gank.gankly.bean.CheckVersion;

/**
 * Create by LingYan on 2016-06-01
 */
public interface ILauncher extends IBaseView {
    void refillDate();

    void callUpdate(CheckVersion checkVersion);
}
