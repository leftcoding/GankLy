package com.gank.gankly.ui.main.meizi.dailymeizi;

import android.text.TextUtils;

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

import rx.Subscriber;
import rx.functions.Func1;

/**
 * Create by LingYan on 2016-10-26
 * Email:137387869@qq.com
 */

public class DailyMeiziPresenter extends FetchPresenter implements DailyMeiziContract.Presenter {
    private MeiziDataSource mTask;
    private DailyMeiziContract.View mModelView;
    private int progress;
    private ArrayList<GiftBean> imagesList;

    public DailyMeiziPresenter(MeiziDataSource task, DailyMeiziContract.View view) {
        mTask = task;
        mModelView = view;
    }

    @Override
    public void fetchNew() {
        mRxManager.add(mTask.fetchDailyGirls()
                .subscribe(new Subscriber<Document>() {
                    @Override
                    public void onCompleted() {
                        mModelView.showContent();
                        mModelView.hideRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                        parseError(mModelView);
                    }

                    @Override
                    public void onNext(Document document) {
                        parseDocument(document);
                    }
                }));
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
        onUnSubscribe();
    }

    @Override
    public void girlsImages(final String url) {
        mRxManager.add(mTask.fetchImageUrls(url)
                .map(new Func1<Document, List<GiftBean>>() {

                    @Override
                    public List<GiftBean> call(Document document) {
                        int max = getImageUrlsMax(document);
                        if (max > 0) {
                            return getImageUrls(url, max);
                        }
                        return null;
                    }
                })
                .subscribe(new Subscriber<List<GiftBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                    }

                    @Override
                    public void onNext(List<GiftBean> list) {
                        if (!ListUtils.isListEmpty(list)) {
                            mModelView.setMaxProgress(list.size());
                            getImages(list);
                        }
                    }
                }));
    }

    private void getImages(List<GiftBean> list) {
        imagesList = new ArrayList<>();
        progress = 0;

        mRxManager.add(mTask.getImageList(list)
                .map(new Func1<Document, ArrayList<GiftBean>>() {
                    @Override
                    public ArrayList<GiftBean> call(Document document) {
                        return getImageCountList(document);
                    }
                })
                .subscribe(new Subscriber<ArrayList<GiftBean>>() {
                    @Override
                    public void onCompleted() {
                        mModelView.disProgressDialog();
                        mModelView.openBrowseActivity(imagesList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mModelView.disProgressDialog();
                        KLog.e(e);
                    }

                    @Override
                    public void onNext(ArrayList<GiftBean> list) {
                        if (ListUtils.getListSize(list) > 0) {
                            mModelView.setProgressValue(progress++);
                            imagesList.addAll(list);
                        }
                    }
                }));
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

    private List<GiftBean> getImageUrls(String url, int max) {
        List<GiftBean> list = new ArrayList<>();
        if (!TextUtils.isEmpty(url) && max > 0) {
            for (int i = 1; i <= max; i++) {
                String u = url + "/" + i;
                list.add(new GiftBean(u));
            }
        }

        return list;
    }

    private ArrayList<GiftBean> getImageCountList(Document doc) {
        ArrayList<GiftBean> list = new ArrayList<>();
        if (doc != null) {
            Elements page = doc.select("#content a img");
            for (int i = 0; i < page.size(); i++) {
                String imgUrl = page.get(i).attr("src");
                list.add(new GiftBean(imgUrl));
            }
        }

        return list;
    }
}
