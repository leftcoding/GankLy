package com.gank.gankly.bean;

/**
 * Create by LingYan on 2016-05-18
 */
public class GiftBean {
    private String imgUrl;
    private String url;
    private String time;
    private String views;
    private String title;

    public GiftBean(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public GiftBean(String imgUrl, String url, String time, String views, String title) {
        this.imgUrl = imgUrl;
        this.url = url;
        this.time = time;
        this.views = views;
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
