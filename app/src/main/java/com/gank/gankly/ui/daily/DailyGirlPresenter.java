package com.gank.gankly.ui.daily;

import android.content.Context;
import android.ly.business.domain.Gift;
import android.ly.jsoup.JsoupServer;

import android.ly.business.domain.Girl;
import com.gank.gankly.utils.ListUtils;
import com.socks.library.KLog;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2016-10-26
 */

public class DailyGirlPresenter extends DailyGirlContract.Presenter {
    private static final String MEIZI_FIRST_URL = "http://m.mzitu.com/all";
    private final DailyGirlContract.View mModelView;
    private ArrayList<Gift> imagesList;
    private int max;

    public DailyGirlPresenter(Context context, DailyGirlContract.View view) {
        super(context, view);
        mModelView = view;
    }

//    @Override
    public void fetchNew() {
//        setFetchLimit(24);
        fetchData(MEIZI_FIRST_URL);
    }

//    @Override
    public void fetchMore() {
        //empty
    }

    private void fetchData(String url) {
        JsoupServer.rxConnect(url).build()
                .subscribe(new Observer<Document>() {
                    @Override
                    public void onComplete() {
//                        setFetchPage(getFetchPage() + 1);
                        mModelView.showContent();
                        mModelView.hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
//                        parseError(mModelView);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Document document) {
                        parseDocument(document);
                    }
                });
    }

    @Override
    public void destroy() {
        //empty
    }

    @Override
    protected void onDestroy() {

    }

    @Override
    public void girlsImages(final String url) {
        JsoupServer.rxConnect(url).build()
                .map(document -> {
                    max = getImageUrlsMax(document);
                    if (max > 0) {
                        return getImageUrl(url);
                    }
                    return null;
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onComplete() {
                        //empty
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        //empty
                    }

                    @Override
                    public void onNext(String url) {
                        mModelView.setMaxProgress(max);
                        getImages(url);
                    }
                });
    }

    private void getImages(String url) {
        JsoupServer.rxConnect(url).build()
                .map(document -> getImageCountList(document))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onComplete() {
                        mModelView.disProgressDialog();
                        mModelView.openBrowseActivity(imagesList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mModelView.disProgressDialog();
                        KLog.e(e);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        //empty
                    }

                    @Override
                    public void onNext(String url) {
                        imagesList = new ArrayList<>();
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
                        for (int i = 1; i <= max; i++) {
                            if (i < 10) {
                                number = "0" + i;
                            } else {
                                number = String.valueOf(i);
                            }
                            lastUrl = baseUrl + name + number + endType;
                            imagesList.add(new Gift(lastUrl));
                        }
                    }
                });
    }

    private void parseDocument(Document document) {
        if (document != null) {
            List<Girl> list = getDays(document);
//            list = filterData(list, mModelView);
            if (ListUtils.getSize(list) > 0) {
                mModelView.refillData(list);
            }
        }
    }

    /**
     * 筛选过滤得到月份集合
     */
    private List<Girl> getDays(Document doc) {
        List<Girl> list = new ArrayList<>();
        if (doc != null) {
            Elements times = doc.select(".post-content .archive-brick");
            Elements a_href = doc.select(".post-content .archive-brick a");
            for (int i = 0; i < a_href.size(); i++) {
                list.add(new Girl(a_href.get(i).attr("href"), times.get(i).text()));
            }
        }
        return list;
    }


    private int getImageUrlsMax(Document doc) {
        int max = 0;
        if (doc != null) {
            Elements page = doc.select(".prev-next-page");
            if (page.size() > 0) {
                String mm = page.get(0).text();
                String[] split = mm.split("/");
                int length = split.length;
                if (length > 1) {
                    String s = split[1];
                    mm = s.substring(0, s.length() - 1);
                    try {
                        max = Integer.parseInt(mm);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        return max;
    }

    private String getImageUrl(String url) {
        return url + "/" + 0;
    }

    private String getImageCountList(Document doc) {
        String imgUrl = null;
        if (doc != null) {
            Elements page = doc.select(".place-padding p a img");
            for (int i = 0; i < page.size(); i++) {
                imgUrl = page.get(i).attr("src");
            }
        }
        return imgUrl;
    }
}
