package com.gank.gankly.mvp.base;

import java.util.List;

/**
 * Create by LingYan on 2016-10-20
 */

public interface PageView<T> extends SupportView {
    void refreshSuccess(List<T> list);

    void refreshFailure(String msg);

    void appendSuccess(List<T> list);

    void appendFailure(String msg);
}
