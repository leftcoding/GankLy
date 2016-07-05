package com.gank.gankly.bean;

/**
 * Create by LingYan on 2016-07-05
 */
public class DailyMeiziBean {
    private String month;
    private String day;
    private String url;
    private String title;

    public DailyMeiziBean(String month, String day, String url, String title) {
        this.month = month;
        this.day = day;
        this.url = url;
        this.title = title;
    }

    public DailyMeiziBean(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
