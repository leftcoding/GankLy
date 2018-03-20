package com.gank.gankly.ui.girls.pure;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.gank.gankly.R;
import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.mvp.source.remote.MeiziDataSource;
import com.gank.gankly.utils.CrashUtils;
import com.gank.gankly.utils.StringUtils;
import com.leftcoding.http.bean.PageConfig;
import com.socks.library.KLog;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2016-12-27
 * Email:137387869@qq.com
 */

public class PurePresenter extends PureContract.Presenter {
    //    private static String BASE_URL = "http://www.mzitu.com/mm";
    //http://www.ygdy8.net/html/gndy/china/list_4_1.html
    private static final String URL = "http://www.ygdy8.net/html/gndy/china/list_4";
    private static final String HOST = "http://www.ygdy8.net";
    private static final String BASE_URL = HOST + "/html/gndy/china/index.html";
    private String nextUrl = BASE_URL + "/page/";

    private List<String> pageUrls = new ArrayList<>();
    private List<String> remoteUrl = new ArrayList<>();
    private List<String> ftpUrls = new ArrayList<>();

    private int mMaxPageNumber;
    private PageConfig mPageConfig;
    private int urlIndex = 0;
    private int pageUrlIndex = 0;


    public PurePresenter(Context context, PureContract.View view) {
        super(context, view);
        mPageConfig = new PageConfig();
        mPageConfig.mLimit = 24;
        urlIndex = 0;
        for (int i = 11; i < 43; i++) {
            remoteUrl.add(mContext.getString(R.string.url_format, URL, i));
        }
    }

    @Override
    public void refreshPure() {
        fetchData(0);
    }

    @Override
    public void appendPure() {
        fetchData(0);
    }

    private void fetchData(int index) {
        MeiziDataSource.getInstance().fetchPure(remoteUrl.get(index))
                .doFinally(() -> mView.hideProgress())
                .subscribe(new Observer<Document>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Document document) {
                        Elements content = document.select(".co_content8 table a");
                        for (int i = 0, s = content.size(); i < s; i++) {
                            String href = content.get(i).attr("href");
                            if (!TextUtils.isEmpty(href) && !href.contains("/index.html")) {
                                pageUrls.add(HOST + href);
                            }
                        }

                        pageUrlIndex = 0;
                        parsePageUrl(pageUrlIndex);
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void parsePageUrl(int index) {
        MeiziDataSource.getInstance().fetchPure(pageUrls.get(index))
                .subscribe(new Observer<Document>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Document document) {
                        Elements content = document.select("tbody a");
                        String data = content.get(0).attr("href");
//                        KLog.d("" + data);
                        ftpUrls.add(data);
                        try {
                            writeFileToSDCard(data + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                    }

                    @Override
                    public void onComplete() {
                        pageUrlIndex = index + 1;
                        if (pageUrlIndex < pageUrls.size()) {
                            parsePageUrl(pageUrlIndex);
                        } else {
                            pageUrls.clear();
                            urlIndex = urlIndex + 1;
                            if (urlIndex < remoteUrl.size()) {
                                fetchData(urlIndex);
                            } else {
                                remoteUrl.clear();
                                KLog.d(">>结束");
                            }
                        }
                    }
                });
    }

    // 写一个文件到SDCard
    private void writeFileToSDCard(String data) throws IOException {
        // 比如可以将一个文件作为普通的文档存储，那么先获取系统默认的文档存放根目录
        File parent_path = Environment.getExternalStorageDirectory();

        // 可以建立一个子目录专门存放自己专属文件
        File dir = new File(parent_path.getAbsoluteFile(), "lingyan");
        if (!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(dir.getAbsoluteFile(), "myfile.txt");

        Log.d("文件路径", file.getAbsolutePath());

        // 创建这个文件，如果不存在
        file.createNewFile();

//        FileOutputStream fos = new FileOutputStream(file);
//
//        byte[] buffer = data.getBytes();
//
//         开始写入数据到这个文件。
//        fos.write(buffer, 0, buffer.length);
//        fos.flush();
//        fos.close();

        try {
            //第二个参数意义是说是否以append方式添加内容
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.write(data);
            bw.flush();
            Log.d("文件写入", "成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getImageMaxPage(Document doc) {
        int max = 0;
        if (doc != null) {
            Elements pages = doc.select(".pagenavi a[href]");
            int size = pages.size();
            for (int i = size - 1; i > 0; i--) {
                String page = pages.get(i).text();
                if (StringUtils.isNumeric(page)) {
                    max = Integer.parseInt(page);
                    break;
                }
            }
        }
        return max;
    }

    private String getImageFirstUrl(Document doc) {
        Elements links = doc.select(".main-image img[src$=.jpg]");
        return links.get(0).attr("src");
    }

    private ArrayList<GiftBean> getImages(String url) {
        ArrayList<GiftBean> imagesList = new ArrayList<>();
        String baseUrl = null;
        String name = null;
        String endType = null;
        int lastPointIndex;
        int lastNameIndex;
        if (url.contains(".")) {
            if (url.contains("-")) {
                lastPointIndex = url.lastIndexOf("-");
            } else {
                lastPointIndex = url.lastIndexOf(".");
            }
            lastNameIndex = url.lastIndexOf("/");
            baseUrl = url.substring(0, lastNameIndex);
            name = url.substring(lastNameIndex, lastPointIndex - 2);
            endType = url.substring(lastPointIndex, url.length());
        }

        String number;
        String lastUrl;
        for (int i = 1; i <= mMaxPageNumber; i++) {
            if (i < 10) {
                number = "0" + i;
            } else {
                number = String.valueOf(i);
            }
            lastUrl = baseUrl + name + number + endType;
            imagesList.add(new GiftBean(lastUrl));
        }
        return imagesList;
    }

    private List<GiftBean> getPageLists(Document doc) {
        List<GiftBean> list = new ArrayList<>();
        if (doc == null) {
            return null;
        }
        Elements hrefs = doc.select("#pins > li > a");
        Elements img = doc.select("#pins a img");
        Elements times = doc.select(".time");
        Elements views = doc.select(".view");

        int countSize = hrefs.size();
        int imgSize = img.size();
        int size = countSize > imgSize ? imgSize : countSize;

        if (size > 0) {
            for (int i = 0; i < size; i++) {
                String imgUrl = img.get(i).attr("data-original");
                KLog.d("imgurl:" + imgUrl);
                String title = img.get(i).attr("alt");
                String url = hrefs.get(i).attr("href");
                String time = times.get(i).text();
                String view = views.get(i).text();
                if (!TextUtils.isEmpty(imgUrl) && !TextUtils.isEmpty(url)) {
                    list.add(new GiftBean(imgUrl, url, time, view, title));
                }
            }
        }
        return list;
    }

    private String getUrl(int page) {
        String _url;
        if (page == 1) {
            _url = BASE_URL;
        } else {
            _url = nextUrl + page;
        }
        return _url;
    }

    private int getMaxPageNum(Document doc) {
        int p = 0;
        if (doc != null) {
            Elements count = doc.select(".nav-links a[href]");
            int size = count.size();
            if (size > 0) {
                for (int i = size - 1; i >= 0; i--) {
                    String num = count.get(i).text();
                    if (StringUtils.isNumeric(num)) {
                        try {
                            return Integer.parseInt(num);
                        } catch (IllegalFormatException e) {
                            KLog.e(e);
                            CrashUtils.crashReport(e);
                        }
                    }
                }
            }
        }
        return p;
    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void refreshImages(String url) {
//        mTask.fetchPure(url)
//                .map(document -> {
//                    mMaxPageNumber = getImageMaxPage(document);
//                    String firstUrl = getImageFirstUrl(document);
//                    return getImages(firstUrl);
//                })
//                .subscribe(new Observer<ArrayList<GiftBean>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(ArrayList<GiftBean> list) {
//                        mModelView.openGalleryActivity(list);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        KLog.e(e);
//                        CrashUtils.crashReport(e);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        mModelView.disLoadingDialog();
//                    }
//                });
    }
}
