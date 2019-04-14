package com.gank.gankly.ui.cure;

import android.content.Context;
import android.lectcoding.ui.logcat.Logcat;
import android.ly.business.domain.Gift;
import android.ly.business.domain.PageConfig;
import android.ly.jsoup.JsoupServer;
import android.text.TextUtils;

import com.gank.gankly.utils.CrashUtils;
import com.gank.gankly.utils.StringUtils;
import com.leftcoding.rxbus.RxApiManager;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2016-10-26
 */

class CurePresenter extends CureContract.Presenter {
    private static final String BASE_URL = "http://www.mzitu.com/xinggan/";

    private final ArrayList<Gift> imagesList = new ArrayList<>();
    private final AtomicBoolean destroyFlag = new AtomicBoolean(false);
    private int maxPageNumber;
    private PageConfig pageConfig = new PageConfig();

    CurePresenter(Context context, CureContract.View view) {
        super(context, view);
        pageConfig.limit = 24;
    }

    @Override
    void refreshGirls() {
        fetchData(getUrl(pageConfig.getCurPage()));
    }

    @Override
    void appendGirls() {
        if (maxPageNumber >= pageConfig.getCurPage()) {
            fetchData(getUrl(pageConfig.getCurPage()));
        }
    }

    @Override
    void loadImages(String url) {
        Disposable disposable = JsoupServer.rxConnect(url).build()
                .doFinally(() -> {
                    if (view != null) {
                        view.disProgressDialog();
                    }
                })
                .subscribe(document -> {
                    getDetailMaxPage(document);
                    getImagesList(document);
                }, Logcat::e);
        RxApiManager.get().add(requestTag, disposable);
    }

    private void fetchData(String url) {
        if (destroyFlag.get()) {
            return;
        }

        Disposable disposable = JsoupServer.rxConnect(url).build()
                .doOnSubscribe(dis -> {
                    if (view != null) {
                        view.showProgress();
                    }
                })
                .doFinally(() -> {
                    if (view != null) {
                        view.hideProgress();
                    }
                })
                .subscribe(document -> {
                    maxPageNumber = getMaxPageNum(document);
                    List<Gift> gifts = getPageLists(document);
                    if (gifts != null) {
                        if (view != null) {
                            if (pageConfig.isFirst()) {
                                view.refreshSuccess(gifts);
                            } else {
                                view.appendSuccess(gifts);
                            }
                        }
                    }
                }, Logcat::e);
        RxApiManager.get().add(requestTag, disposable);
    }

    /**
     * 解析每面，封面入口地址
     */
    private List<Gift> getPageLists(Document doc) {
        List<Gift> list = new ArrayList<>();
        if (doc == null) {
            return null;
        }
        Elements href = doc.select("#pins > li > a");
        Elements img = doc.select("#pins a img");
        Elements times = doc.select(".time");
        Elements views = doc.select(".view");

        final int countSize = href.size();
        final int imgSize = img.size();
        final int size = countSize > imgSize ? imgSize : countSize;

        if (size > 0) {
            for (int i = 0; i < size; i++) {
                String imgUrl = img.get(i).attr("data-original");
                String title = img.get(i).attr("alt");
                String url = href.get(i).attr("href");
                String time = times.get(i).text();
                String view = views.get(i).text();
                if (!TextUtils.isEmpty(imgUrl) && !TextUtils.isEmpty(url)) {
                    list.add(new Gift(imgUrl, url, time, view, title));
                }
            }
        }
        return list;
    }

    private int getMaxPageNum(Document doc) {
        int p = 0;
        if (doc != null) {
            Elements count = doc.select(".nav-links a[href]");
            int size = count.size();
            if (size > 0) {
                for (int i = size - 1; i >= 0; i--) {
                    String num = count.get(i).text();
                    if (StringUtils.isNumeric(num)) {
                        try {
                            return Integer.parseInt(num);
                        } catch (IllegalFormatException e) {
                            Logcat.e(e);
                            CrashUtils.crashReport(e);
                        }
                    }
                }
            }
        }
        return p;
    }

    private int getDetailMaxPage(Document doc) {
        if (doc != null) {
            Elements pages = doc.select(".pagenavi span");
            if (pages != null && !pages.isEmpty()) {

            }
        }
        return 0;
    }

    private void getImagesList(Document doc) {
        imagesList.clear();
        if (doc != null) {
            Elements imgs = doc.select(".main-image a img");
            if (imgs != null && imgs.size() > 0) {
                for (int i = 0; i < imgs.size(); i++) {
                    String src = imgs.get(i).attr("src");
                    imagesList.add(new Gift(src));
                }
            }
        }
    }

    private String getUrl(int page) {
        return page == pageConfig.initPage ? BASE_URL : BASE_URL + "page/" + page;
    }

    @Override
    public void destroy() {
        if (destroyFlag.compareAndSet(false, true)) {
            RxApiManager.get().clear(requestTag);
        }
        super.destroy();
    }

    @Override
    protected void onDestroy() {

    }
}
