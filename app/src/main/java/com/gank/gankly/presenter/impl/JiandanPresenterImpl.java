package com.gank.gankly.presenter.impl;

import android.app.Activity;

import com.gank.gankly.bean.JiandanResult;
import com.gank.gankly.model.JiandanModel;
import com.gank.gankly.model.impl.JiandanModelImpl;
import com.gank.gankly.presenter.BaseAsynDataSource;
import com.gank.gankly.presenter.ViewControl;
import com.gank.gankly.utils.CrashUtils;
import com.gank.gankly.utils.ListUtils;
import com.gank.gankly.view.IMeiziView;
import com.socks.library.KLog;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Subscriber;

/**
 * Create by LingYan on 2016-07-20
 * Email:137387869@qq.com
 */
public class JiandanPresenterImpl extends BaseAsynDataSource<IMeiziView<List<JiandanResult.PostsBean>>> {
    private JiandanModel mModel;
    private ViewControl mViewControl = new ViewControl();
    // 获取img标签正则
    private static final String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";
    // 获取src路径的正则
//    private static final String IMGSRC_REG = "http:\"?(.*?)(\"|>|\\s+)";
    private static final String IMGSRC_REG = "http:\"?(.*?)(\"|>|\\s+)+(\\.jpg|\\.gif)";

    public JiandanPresenterImpl(Activity mActivity, IMeiziView<List<JiandanResult.PostsBean>> view) {
        super(mActivity, view);
        mModel = new JiandanModelImpl();
    }

    @Override
    public void fetchNew() {
        super.fetchNew();
        initFirstPage();
        fetchData();
    }

    @Override
    public void fetchData() {
        super.fetchData();
        final int mPage = getNextPage();
        KLog.d("mPage" + mPage);
        mModel.fetchData(mPage, new Subscriber<JiandanResult>() {
            @Override
            public void onCompleted() {
                mIView.hideRefresh();
                mIView.showContent();
                setFirst(false);
                setNextPage(mPage + 1);
            }

            @Override
            public void onError(Throwable e) {
                KLog.e(e);
                mIView.hideRefresh();
                CrashUtils.crashReport(e);
                mViewControl.onError(mPage, isFirst(), isNetworkAvailable(), mIView);
            }

            @Override
            public void onNext(JiandanResult result) {
                mViewControl.onNext(mPage, getLimit(), filterResult(result.getPosts()), mIView, new ViewControl.CallBackViewShow() {
                    @Override
                    public void hasMore(boolean more) {
                        setHasMore(more);
                    }
                });
            }
        });
    }

    private List<JiandanResult.PostsBean> filterResult(List<JiandanResult.PostsBean> list) {
        if (!ListUtils.isListEmpty(list)) {
            int size = list.size();
            KLog.d("size:" + size);
            String quote;
            for (int i = 0; i < size; i++) {
                String content = list.get(i).getContent();
                Pattern p = Pattern.compile("<img\\b[^>]*\\bsrc\\b\\s*=\\s*('|\")?([^'\"\n\r\f>]+(\\.jpg|\\.bmp|\\.eps|\\.gif|\\.mif|\\.miff|\\.png|\\.tif|\\.tiff|\\.svg|\\.wmf|\\.jpe|\\.jpeg|\\.dib|\\.ico|\\.tga|\\.cut|\\.pic)\\b)[^>]*>", Pattern.CASE_INSENSITIVE);
                Matcher matcher = p.matcher(content);

                if (matcher.find()) {
                    quote = matcher.group(1);
                    String c = (quote == null || quote.trim().length() == 0) ? matcher.group(2).split("\\s+")[0] : matcher.group(2);
                    if (!c.startsWith("http:")) {
                        c = "http:" + c;
                    }
                    list.get(i).setImg(c);
                }
            }
        }
        return list;
    }

    @Override
    public void fetchMore() {
        super.fetchMore();
        mIView.showRefresh();
        fetchData();
    }
}
