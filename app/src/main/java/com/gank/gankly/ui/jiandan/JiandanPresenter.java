package com.gank.gankly.ui.jiandan;

import com.gank.gankly.bean.JiandanBean;
import com.gank.gankly.mvp.FetchPresenter;
import com.gank.gankly.mvp.source.remote.JiandanDataSource;
import com.gank.gankly.utils.ListUtils;
import com.socks.library.KLog;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Create by LingYan on 2016-11-21
 * Email:137387869@qq.com
 */

public class JiandanPresenter extends FetchPresenter implements JiandanContract.Presenter {
    private static final String BASE_URL = "http://i.jandan.net/page/";
    private static final int LIMIT = 24;

    private JiandanDataSource mJiandanDataSource;
    private JiandanContract.View mView;

    public JiandanPresenter(JiandanDataSource jiandanDataSource, JiandanContract.View view) {
        mJiandanDataSource = jiandanDataSource;
        mView = view;
        setFetchLimit(LIMIT);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    public void fetchData(int page) {
        String url = BASE_URL + page;
        mJiandanDataSource.fetchData(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Document, List<JiandanBean>>() {
                    @Override
                    public List<JiandanBean> call(Document document) {
                        return mapResult(document);
                    }
                })
                .subscribe(new Subscriber<List<JiandanBean>>() {
                    @Override
                    public void onCompleted() {
                        mView.showContent();
                        int nextPage = getFetchPage() + 1;
                        setFetchPage(nextPage);
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                    }

                    @Override
                    public void onNext(List<JiandanBean> list) {
                        list = filterData(list, mView);
                        if (ListUtils.getListSize(list) > 0) {
                            if (getFetchPage() > 1) {
                                mView.appendMoreDate(list);
                            } else {
                                mView.refillData(list);
                            }
                        }
                    }
                });
    }

    private List<JiandanBean> mapResult(Document doc) {
        List<JiandanBean> list = new ArrayList<>();
        if (doc != null) {
            Elements herfs = doc.select(".thumb_s a");
            Elements imgs = doc.select(".thumb_s a img");
            Elements types = doc.select(".indexs");
            Elements titles = doc.select(".thetitle");

            String url;
            String title;
            String type;
            String imgUrl;
            for (int i = 0; i < herfs.size(); i++) {
                url = herfs.get(i).attr("href");
                title = titles.get(i).text();
                type = types.get(i).text();
                imgUrl = imgs.get(i).attr("data-original");
                if (imgUrl != null && imgUrl.startsWith("//")) {
                    imgUrl = imgUrl.substring(2, imgUrl.length());
                    imgUrl = "http://" + imgUrl;
                }
                list.add(new JiandanBean(url, title, type, imgUrl));
            }
        }
        return list;
    }

    @Override
    public void fetchNew() {
        fetchData(getInitPage());
    }

    @Override
    public void fetchMore() {
        if (isHasMore()) {
            mView.showRefresh();
            fetchData(getFetchPage());
        }
    }
}
