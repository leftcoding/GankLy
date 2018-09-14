package com.gank.gankly.ui.discovered.jiandan;

import android.content.Context;
import android.ly.jsoup.JsoupServer;

import com.gank.gankly.bean.JianDanBean;
import com.socks.library.KLog;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Create by LingYan on 2016-11-21
 */

public class JiandanPresenter extends JiandanContract.Presenter {
    private static final String BASE_URL = "http://i.jandan.net/page/";
    private static final int LIMIT = 24;

    private JiandanContract.View mView;

    public JiandanPresenter(Context context, JiandanContract.View view) {
        super(context, view);
    }

    @Override
    public void destroy() {

    }

    @Override
    protected void onDestroy() {

    }

    public void fetchData(int page) {
        String url = BASE_URL + page;
        JsoupServer.rxConnect(url).build()
                .map(document -> mapResult(document))
                .subscribe(new Observer<List<JianDanBean>>() {
                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                    }

                    @Override
                    public void onComplete() {
                        mView.showContent();
//                        int nextPage = getFetchPage() + 1;
//                        setFetchPage(nextPage);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<JianDanBean> list) {
//                        list = filterData(list, mView);
//                        if (ListUtils.getSize(list) > 0) {
//                            if (getFetchPage() > 1) {
//                                mView.appendMoreDate(list);
//                            } else {
//                                mView.refillData(list);
//                            }
//                        }
                    }
                });
    }

    private List<JianDanBean> mapResult(Document doc) {
        List<JianDanBean> list = new ArrayList<>();
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
                list.add(new JianDanBean(url, title, type, imgUrl));
            }
        }
        return list;
    }

//    @Override
//    public void fetchNew() {
//        fetchData(getInitPage());
//    }
//
//    @Override
//    public void fetchMore() {
//        if (hasMore()) {
//            mView.showProgress();
//            fetchData(getFetchPage());
//        }
//    }
}
