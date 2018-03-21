package android.ly.business.domain;

/**
 * Create by LingYan on 2017-11-18
 */

public class PageConfig {
    /**
     * 分页个数 默认20
     */
    public int limit = 20;

    /**
     * 初始化请求页数
     */
    public int initPage = 1;

    /**
     * 当前请求页数
     */
    public int curPage = 1;

    public PageConfig() {

    }

    public PageConfig(int limit, int initPage, int curPage) {
        this.limit = limit;
        this.initPage = initPage;
        this.curPage = curPage;
    }
}
