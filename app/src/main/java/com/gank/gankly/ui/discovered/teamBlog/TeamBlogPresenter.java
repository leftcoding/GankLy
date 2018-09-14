package com.gank.gankly.ui.discovered.teamBlog;

import android.content.Context;
import android.ly.jsoup.JsoupServer;

import com.gank.gankly.bean.JianDanBean;
import com.gank.gankly.mvp.source.remote.TeamBlogDataSource;
import com.gank.gankly.ui.discovered.technology.TechnologyContract;
import com.socks.library.KLog;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2016-11-23
 */

public class TeamBlogPresenter extends TechnologyContract.Presenter {
    private TeamBlogDataSource mTask;
    private TechnologyContract.View mView;

    TeamBlogPresenter(Context context, TechnologyContract.View view) {
        super(context, view);
    }

    private void fetchData(int page) {
        String url = "";
        JsoupServer.rxConnect(url).build().subscribe(new Observer<Document>() {
            @Override
            public void onComplete() {
                mView.showContent();
                mView.hideProgress();
            }

            @Override
            public void onError(Throwable e) {
                mView.hideProgress();
                KLog.e(e);
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Document document) {
                List<JianDanBean> list = parseDocument(document);
            }
        });
    }

    private List<JianDanBean> parseDocument(Document document) {
        List<JianDanBean> jiandanBeen = new ArrayList<>();
        if (document != null) {
            Elements hrefs = document.select(".xiandu_left a");
            Elements imgs = document.select(".xiandu_right img");
            Elements titles = document.select(".xiandu_item");
            if (hrefs != null) {
                String href;
                String title;
                String img;
                String type = null;
                for (int i = 0; i < hrefs.size(); i++) {
                    href = hrefs.get(i).attr("href");
                    img = imgs.get(i).attr("src");
                    title = titles.get(i).text();
                    jiandanBeen.add(new JianDanBean(href, title, type, img));
                }
            }
        }
        return jiandanBeen;
    }

    @Override
    public void destroy() {

    }

    @Override
    protected void onDestroy() {

    }
}
