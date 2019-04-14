package android.ly.jsoup;

/**
 * Create by LingYan on 2018-03-23
 */

public class JsoupServer {
    private JsoupServer() {
    }

    public static JsoupMate connect(String url) {
        return new JsoupMate(url);
    }

    public static RxJsoupMate rxConnect(String url) {
        return new RxJsoupMate(url);
    }
}
