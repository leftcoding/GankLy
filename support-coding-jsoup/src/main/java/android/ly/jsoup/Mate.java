package android.ly.jsoup;

/**
 * Create by LingYan on 2018-03-23
 */

abstract class Mate<T> {
    static final int TIME_OUT = 50 * 1000;
    static final String USER_AGENT = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.76 Mobile Safari/537.36";
    static final String DESKTOP_USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; Desktop) AppleWebKit/534.13 (KHTML, like Gecko) UCBrowser/8.9.0.25";

    int timeOut = TIME_OUT;
    String userAgent = USER_AGENT;
    String url;
    boolean isMobile = true;

    Mate(String url) {
        this.url = url;
    }

    public abstract T build();

    public abstract Mate setTimeOut(int timeOut);

    public abstract Mate setUserAgent(String userAgent);

    public abstract Mate setMobile(boolean isMobile);
}
