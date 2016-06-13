package com.gank.gankly.ui.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.bean.GiftResult;
import com.gank.gankly.ui.view.IGiftView;
import com.gank.gankly.utils.StringUtils;
import com.socks.library.KLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Create by LingYan on 2016-06-13
 */
public class GiftPresenter extends BasePresenter<IGiftView> {
    private static final int timeout = 50 * 1000;
    private static final String DESKTOP_USERAGENT = "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; Desktop) AppleWebKit/534.13 (KHTML, like Gecko) UCBrowser/8.9.0.25";
    private String url = "http://www.mzitu.com/mm/page/";
    private int mPages;

    public GiftPresenter(Activity mActivity, IGiftView view) {
        super(mActivity, view);
    }

    public void fetchPageCount(final int mCurPage) {
        Observable<GiftResult> observable = Observable.create(new Observable.OnSubscribe<GiftResult>() {
            @Override
            public void call(Subscriber<? super GiftResult> subscriber) {
                try {
                    String _url = url + mCurPage;
                    Document doc = Jsoup.connect(_url)
                            .userAgent(DESKTOP_USERAGENT)
                            .timeout(timeout)
                            .get();
                    int num = getBigPageNum(doc);
                    subscriber.onNext(new GiftResult(num, getPageCount(doc)));
                } catch (IOException e) {
                    KLog.e(e);
                }
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Subscriber<GiftResult>() {
            @Override
            public void onCompleted() {
                mIView.hideRefresh();
                mIView.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                mIView.hideRefresh();
                KLog.e(e);
            }

            @Override
            public void onNext(GiftResult giftResult) {
                if (giftResult != null) {
                    if (giftResult.getSize() > 0) {
                        if (mCurPage == 1) {
                            mIView.clear();
                        }
                        mIView.refillDate(giftResult.getList());
                    }
                    mPages = giftResult.getNum();
                }
            }
        });
    }

    private int getBigPageNum(Document doc) {
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
                        }
                    }
                }
            }
        }
        return p;
    }

    private List<GiftBean> getPageCount(Document doc) {
        List<GiftBean> list = null;
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
            list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                String imgUrl = img.get(i).attr("data-original");
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

    public int getPages() {
        return mPages;
    }
}
