package android.ly.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Create by LingYan on 2018-03-23
 */

public class JsoupMate extends Mate<Document> {

    public JsoupMate(String url) {
        super(url);
    }

    @Override
    public Document build() {
        try {
            return Jsoup.connect(url).timeout(timeOut).userAgent(userAgent).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
