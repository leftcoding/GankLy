package com.gank.gankly.model.impl;

import com.gank.gankly.bean.DailyMeiziBean;
import com.gank.gankly.model.BaseMeziModel;
import com.gank.gankly.model.DailyMeiziModel;
import com.socks.library.KLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Create by LingYan on 2016-07-05
 */
public class DailyMeiziModelImpl extends BaseMeziModel implements DailyMeiziModel {
    public DailyMeiziModelImpl() {

    }

    @Override
    public void fetchDailyMeizi(Subscriber<List<DailyMeiziBean>> subscriber) {
        Observable<List<DailyMeiziBean>> observable = Observable.create(new Observable.OnSubscribe<List<DailyMeiziBean>>() {
            @Override
            public void call(Subscriber<? super List<DailyMeiziBean>> subscriber) {
                try {
                    String _url = "http://www.mzitu.com/all";
                    Document doc = Jsoup.connect(_url)
                            .userAgent(USERAGENT)
                            .timeout(timeout)
                            .get();
                    subscriber.onNext(getMonthList(doc));
                } catch (IOException e) {
                    KLog.e(e);
                }
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(subscriber);
    }

    private List<DailyMeiziBean> getMonthList(Document doc) {
        List<DailyMeiziBean> list = new ArrayList<>();
        if (doc != null) {
            Elements times = doc.select(".post-content .archive-brick");
            Elements ahref = doc.select(".post-content .archive-brick a");
            for (int i = 0; i < ahref.size(); i++) {
                KLog.d("" + times.get(i).text());
                KLog.d("ahref:" + ahref.get(i).attr("href"));
                list.add(new DailyMeiziBean(ahref.get(i).attr("href"), times.get(i).text()));
            }
        }
        return list;
    }
}
