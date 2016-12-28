package com.gank.gankly.ui.main.meizi.dailymeizi;

import com.gank.gankly.bean.DailyMeiziBean;
import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.mvp.FetchPresenter;
import com.gank.gankly.mvp.source.remote.MeiziDataSource;
import com.gank.gankly.utils.ListUtils;
import com.socks.library.KLog;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Create by LingYan on 2016-10-26
 * Email:137387869@qq.com
 */

public class DailyMeiziPresenter extends FetchPresenter implements DailyMeiziContract.Presenter {
    private MeiziDataSource mTask;
    private DailyMeiziContract.View mModelView;
    private ArrayList<GiftBean> imagesList;
    private int max;

    public DailyMeiziPresenter(MeiziDataSource task, DailyMeiziContract.View view) {
        mTask = task;
        mModelView = view;
    }

    @Override
    public void fetchNew() {
        mTask.fetchDaily()
                .subscribe(new Observer<Document>() {
                    @Override
                    public void onComplete() {
                        mModelView.showContent();
                        mModelView.hideRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                        parseError(mModelView);
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
    public void fetchMore() {
        //empty
    }

    @Override
    public void subscribe() {
        //empty
    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void girlsImages(final String url) {
        mTask.fetchDailyDays(url)
                .map(new Function<Document, String>() {
                    @Override
                    public String apply(Document document) throws Exception {
                        max = getImageUrlsMax(document);
                        if (max > 0) {
                            return getImageUrl(url);
                        }
                        return null;
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String url) {
                        mModelView.setMaxProgress(max);
                        getImages(url);
                    }
                });
    }

    private void getImages(String url) {
        mTask.fetchDailyDetailUrls(url)
                .map(new Function<Document, String>() {
                    @Override
                    public String apply(Document document) throws Exception {
                        return getImageCountList(document);
                    }
                })
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
                            imagesList.add(new GiftBean(lastUrl));
                        }
                    }
                });
    }


    private void parseDocument(Document document) {
        if (document != null) {
            List<DailyMeiziBean> list = getDays(document);
            if (ListUtils.getListSize(list) > 0) {
                mModelView.refillData(list);
            }
        }
    }

    /**
     * 筛选过滤得到月份集合
     */
    private List<DailyMeiziBean> getDays(Document doc) {
        List<DailyMeiziBean> list = new ArrayList<>();
        if (doc != null) {
            Elements times = doc.select(".post-content .archive-brick");
            Elements a_href = doc.select(".post-content .archive-brick a");
            for (int i = 0; i < a_href.size(); i++) {
                list.add(new DailyMeiziBean(a_href.get(i).attr("href"), times.get(i).text()));
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
