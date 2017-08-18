package com.gank.gankly.ui.girls.pure;

import android.text.TextUtils;

import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.mvp.FetchPresenter;
import com.gank.gankly.mvp.source.remote.MeiziDataSource;
import com.gank.gankly.utils.CrashUtils;
import com.gank.gankly.utils.StringUtils;
import com.socks.library.KLog;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2016-12-27
 * Email:137387869@qq.com
 */

public class PurePresenter extends FetchPresenter implements PureContract.Presenter {
    private static String BASE_URL = "http://www.mzitu.com/mm";
    private String nextUrl = BASE_URL + "/page/";

    private MeiziDataSource mTask;
    private PureContract.View mModelView;

    private int mMaxPageNumber;

    public PurePresenter(MeiziDataSource task, PureContract.View view) {
        this.mTask = task;
        this.mModelView = view;
    }

    @Override
    public void fetchNew() {
        setFetchLimit(24);
        fetchData(getInitPage());
    }

    @Override
    public void fetchMore() {
        if (hasMore()) {
            mModelView.showRefresh();
            fetchData(getFetchPage());
        }
    }

    private void fetchData(final int page) {
        KLog.d("fetchData");
        String url = getUrl(page);
        mTask.fetchPure(url)
                .subscribe(new Observer<Document>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Document document) {
//                        KLog.d(document);
                        mMaxPageNumber = getMaxPageNum(document);
                        List<GiftBean> list = getPageLists(document);
                        list = filterData(list, mModelView);
                        if (list != null) {
                            if (page == 1) {
                                mModelView.refillData(list);
                            } else {
                                mModelView.appendData(list);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                        CrashUtils.crashReport(e);
                        parseError(mModelView);
                    }

                    @Override
                    public void onComplete() {
                        mModelView.showContent();
                        mModelView.hideRefresh();
                        setFetchPage(page + 1);
                    }
                });
    }

    private int getImageMaxPage(Document doc) {
        int max = 0;
        if (doc != null) {
            Elements pages = doc.select(".pagenavi a[href]");
            int size = pages.size();
            for (int i = size - 1; i > 0; i--) {
                String page = pages.get(i).text();
                if (StringUtils.isNumeric(page)) {
                    max = Integer.parseInt(page);
                    break;
                }
            }
        }
        return max;
    }

    private String getImageFirstUrl(Document doc) {
        Elements links = doc.select(".main-image img[src$=.jpg]");
        return links.get(0).attr("src");
    }

    private ArrayList<GiftBean> getImages(String url) {
        ArrayList<GiftBean> imagesList = new ArrayList<>();
        String baseUrl = null;
        String name = null;
        String endType = null;
        int lastPointIndex;
        int lastNameIndex;
        if (url.contains(".")) {
            if (url.contains("-")) {
                lastPointIndex = url.lastIndexOf("-");
            } else {
                lastPointIndex = url.lastIndexOf(".");
            }
            lastNameIndex = url.lastIndexOf("/");
            baseUrl = url.substring(0, lastNameIndex);
            name = url.substring(lastNameIndex, lastPointIndex - 2);
            endType = url.substring(lastPointIndex, url.length());
        }

        String number;
        String lastUrl;
        for (int i = 1; i <= mMaxPageNumber; i++) {
            if (i < 10) {
                number = "0" + i;
            } else {
                number = String.valueOf(i);
            }
            lastUrl = baseUrl + name + number + endType;
            imagesList.add(new GiftBean(lastUrl));
        }
        return imagesList;
    }

    private List<GiftBean> getPageLists(Document doc) {
        List<GiftBean> list = new ArrayList<>();
        if (doc == null) {
            return null;
        }
        Elements hrefs = doc.select("#pins > li > a");
        Elements img = doc.select("#pins a img");
        Elements times = doc.select(".time");
        Elements views = doc.select(".view");

        int countSize = hrefs.size();
        int imgSize = img.size();
        int size = countSize > imgSize ? imgSize : countSize;

        if (size > 0) {
            for (int i = 0; i < size; i++) {
                String imgUrl = img.get(i).attr("data-original");
                KLog.d("imgurl:"+imgUrl);
                String title = img.get(i).attr("alt");
                String url = hrefs.get(i).attr("href");
                String time = times.get(i).text();
                String view = views.get(i).text();
                if (!TextUtils.isEmpty(imgUrl) && !TextUtils.isEmpty(url)) {
                    list.add(new GiftBean(imgUrl, url, time, view, title));
                }
            }
        }
        return list;
    }

    private String getUrl(int page) {
        String _url;
        if (page == 1) {
            _url = BASE_URL;
        } else {
            _url = nextUrl + page;
        }
        return _url;
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
                            KLog.e(e);
                            CrashUtils.crashReport(e);
                        }
                    }
                }
            }
        }
        return p;
    }


    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void fetchImages(String url) {
        mTask.fetchPure(url)
                .map(document -> {
                    mMaxPageNumber = getImageMaxPage(document);
                    String firstUrl = getImageFirstUrl(document);
                    return getImages(firstUrl);
                })
                .subscribe(new Observer<ArrayList<GiftBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<GiftBean> list) {
                        mModelView.openGalleryActivity(list);
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                        CrashUtils.crashReport(e);
                    }

                    @Override
                    public void onComplete() {
                        mModelView.disLoadingDialog();
                    }
                });
    }
}
