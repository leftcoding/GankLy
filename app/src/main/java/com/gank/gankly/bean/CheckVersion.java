package com.gank.gankly.bean;

/**
 * Create by LingYan on 2016-06-01
 */
public class CheckVersion {
    private boolean must;
    private int code;
    private String version;
    private String url;
    private String changelog;
    private long appLength;
    private String appSize;

    public boolean isMust() {
        return must;
    }

    public String getAppSize() {
        return appSize;
    }

    public long getAppLength() {
        return appLength;
    }

    public String getChangelog() {
        return changelog;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
