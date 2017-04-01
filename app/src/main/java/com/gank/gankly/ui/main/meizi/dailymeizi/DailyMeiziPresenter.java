package com.gank.gankly.ui.main.meizi.dailymeizi;

import com.gank.gankly.R;
import com.gank.gankly.bean.DailyMeiziBean;
import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.mvp.FetchPresenter;
import com.gank.gankly.mvp.source.remote.MeiziDataSource;
import com.gank.gankly.utils.ListUtils;
import com.gank.gankly.utils.ToastUtils;
import com.socks.library.KLog;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2016-10-26
 * Email:137387869@qq.com
 */

public class DailyMeiziPresenter extends FetchPresenter implements DailyMeiziContract.Presenter {
    private static final String MEIZI_FIRST_URL = "http://m.mzitu.com/all";
    private static final String MEIZI_SECOND_URL = "http://m.mzitu.com/page/";
    private final MeiziDataSource mTask;
    private final DailyMeiziContract.View mModelView;
    private ArrayList<GiftBean> imagesList;
    private int max;

    public DailyMeiziPresenter(MeiziDataSource task, DailyMeiziContract.View view) {
        mTask = task;
        mModelView = view;
    }

    @Override
    public void fetchNew() {
        setFetchLimit(24);
        fetchData(MEIZI_FIRST_URL);
    }

    @Override
    public void fetchMore() {
        if (hasMore()) {
            mModelView.showLoading();
            String url = MEIZI_SECOND_URL + getFetchPage();
            fetchData(url);
        } else {
            ToastUtils.showToast(R.string.loading_all_over);
        }
    }

    private void fetchData(String url) {
        mTask.fetchDaily(url)
                .subscribe(new Observer<Document>() {
                    @Override
                    public void onComplete() {
                        setFetchPage(getFetchPage() + 1);
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
    public void subscribe() {
        //empty
    }

    @Override
    public void unSubscribe() {
        //empty
    }

    @Override
    public void girlsImages(final String url) {
        mTask.fetchDailyDays(url)
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
        mTask.fetchDailyDetailUrls(url)
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
                            imagesList.add(new GiftBean(lastUrl));
                        }
                    }
                });
    }

    private void parseDocument(Document document) {
        if (document != null) {
            List<DailyMeiziBean> list = getDays(document);
            list = filterData(list, mModelView);
            if (ListUtils.getListSize(list) > 0) {
                int page = getFetchPage();
                if (page == 1) {
                    mModelView.refillData(list);
                } else {
                    mModelView.appendItem(list);
                }
            }
        }
    }

    /**
     * 筛选过滤得到月份集合
     */
    private List<DailyMeiziBean> getDays(Document doc) {
        List<DailyMeiziBean> list = new ArrayList<>();
        if (doc != null) {
            Elements a_href = doc.select(".place-padding h2 a");
            for (int i = 0; i < a_href.size(); i++) {
                list.add(new DailyMeiziBean(a_href.get(i).attr("href"), a_href.get(i).text()));
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
