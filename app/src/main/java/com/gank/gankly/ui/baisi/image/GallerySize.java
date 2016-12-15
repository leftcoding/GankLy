package com.gank.gankly.ui.baisi.image;

/**
 * Create by LingYan on 2016-12-12
 * Email:137387869@qq.com
 */

public class GallerySize {
    private int height;
    private int width;
    private String url;
    private int locationY;
    private int position;
    private String title;
    private String shareUrl;

    public GallerySize(int height, int width, String url, int locationY, String title) {
        this.height = height;
        this.width = width;
        this.url = url;
        this.locationY = locationY;
        this.title = title;
    }

    public GallerySize(int height, int width, String url, int position) {
        this.height = height;
        this.width = width;
        this.url = url;
        this.position = position;
    }

    public GallerySize(int height, int width, String url) {
        this.height = height;
        this.width = width;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public GallerySize(int height, int width, String url, int position, String title, String shareUrl) {
        this.height = height;
        this.width = width;
        this.url = url;
        this.position = position;
        this.title = title;
        this.shareUrl = shareUrl;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public GallerySize(String url) {
        this.url = url;
    }

    public int getLocationY() {
        return locationY;
    }

    public void setLocationY(int locationY) {
        this.locationY = locationY;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
