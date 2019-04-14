package android.ly.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by LingYan on 2018-03-23
 */

public class RxJsoupMate extends Mate<Observable<Document>> {

    RxJsoupMate(String url) {
        super(url);
    }

    @Override
    public Observable<Document> build() {
        return Observable.create(new ObservableOnSubscribe<Document>() {
            @Override
            public void subscribe(ObservableEmitter<Document> subscriber) throws Exception {
                Document doc;
                try {
                    doc = Jsoup.connect(url)
                            .userAgent(userAgent)
                            .timeout(timeOut)
                            .get();
                    subscriber.onNext(doc);
                } catch (IOException e) {
                    subscriber.onError(new Throwable(e));
                }
                subscriber.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Mate setTimeOut(int timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    @Override
    public Mate setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    @Override
    public Mate setMobile(boolean isMobile) {
        this.isMobile = isMobile;
        return this;
    }
}
