package com.gank.gankly.network;

public interface DownloadProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}