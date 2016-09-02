package com.gank.gankly.ui.main;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.RxBus.ChangeThemeEvent.ThemeEvent;
import com.gank.gankly.RxBus.RxBus;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.config.Constants;
import com.gank.gankly.listener.RecyclerOnClick;
import com.gank.gankly.presenter.IBaseRefreshPresenter;
import com.gank.gankly.presenter.impl.IosGoodsPresenterImpl;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.web.WebActivity;
import com.gank.gankly.view.IMeiziView;
import com.gank.gankly.widget.MultipleStatusView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * ios
 * Create by LingYan on 2016-4-26
 * Email:137387869@qq.com
 */
public class IosFragment extends LazyFragment implements SwipeRefreshLayout.OnRefreshListener,
        RecyclerOnClick, IMeiziView<List<ResultsBean>> {
    @BindView(R.id.meizi_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.meizi_swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.meizi_multiple_status_view)
    MultipleStatusView mMultipleStatusView;

    private MainActivity mActivity;
    private GankAdapter mRecyclerAdapter;
    private IBaseRefreshPresenter mPresenter;

    private int mLastPosition;

    public IosFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (MainActivity) context;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new IosGoodsPresenterImpl(mActivity, this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    protected void initValues() {
        setRecyclerViewBackground();

        RxBus.getInstance().toSubscription(ThemeEvent.class, new Action1<ThemeEvent>() {
            @Override
            public void call(ThemeEvent event) {
                refreshUi();
            }
        });
    }

    @Override
    protected void initViews() {
        mRecyclerAdapter = new GankAdapter(mActivity, GankAdapter.LAYOUT_IOS);
        mRecyclerAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setHasFixedSize(true);
        mSwipeRefreshLayout.setColorSchemeColors(App.getAppColor(R.color.colorPrimary));
    }

    @Override
    protected void bindLister() {
        mMultipleStatusView.setListener(new MultipleStatusView.OnMultipleClick() {
            @Override
            public void retry(View v) {
                initFetchDate();
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && mLastPosition + 1 == mRecyclerAdapter.getItemCount()
                        && !mSwipeRefreshLayout.isRefreshing()) {
                    mPresenter.fetchMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                mLastPosition = layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_meizi;
    }

    @Override
    protected void initData() {
        showLoading();
        initFetchDate();
    }

    private void initFetchDate() {
        mPresenter.fetchNew();
    }

    private void setRecyclerViewBackground() {
        if (App.isNight()) {
            mRecyclerView.setBackgroundResource(R.color.dark_background);
        } else {
            mRecyclerView.setBackgroundResource(R.color.base_refresh_list_bg);
        }
    }

    public void refreshUi() {
        setRecyclerViewBackground();

        Resources.Theme theme = mActivity.getTheme();
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(R.attr.baseAdapterItemBackground, typedValue, true);
        int background = typedValue.data;
        theme.resolveAttribute(R.attr.baseAdapterItemTextColor, typedValue, true);
        int textColor = typedValue.data;

        int childCount = mRecyclerView.getChildCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            ViewGroup childView = (ViewGroup) mRecyclerView.getChildAt(childIndex);
            childView.setBackgroundColor(background);
            TextView nickName = (TextView) childView.findViewById(R.id.goods_txt_title);
            nickName.setTextColor(textColor);
        }

        //让 RecyclerView 缓存在 Pool 中的 Item 失效
        //那么，如果是ListView，要怎么做呢？这里的思路是通过反射拿到 AbsListView 类中的 RecycleBin 对象，然后同样再用反射去调用 clear 方法
        Class<RecyclerView> recyclerViewClass = RecyclerView.class;
        try {
            Field declaredField = recyclerViewClass.getDeclaredField("mRecycler");
            declaredField.setAccessible(true);
            Method declaredMethod = Class.forName(RecyclerView.Recycler.class.getName()).getDeclaredMethod("clear", (Class<?>[]) new Class[0]);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(declaredField.get(mRecyclerView), new Object[0]);
            RecyclerView.RecycledViewPool recycledViewPool = mRecyclerView.getRecycledViewPool();
            recycledViewPool.clear();

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static IosFragment newInstance() {
        IosFragment fragment = new IosFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onRefresh() {
        showRefresh();
        initFetchDate();
    }

    @Override
    public void onClick(View view, ResultsBean bean) {
        Bundle bundle = new Bundle();
        bundle.putString(WebActivity.TITLE, bean.getDesc());
        bundle.putString(WebActivity.URL, bean.getUrl());
        bundle.putString(WebActivity.TYPE, Constants.IOS);
        bundle.putString(WebActivity.AUTHOR, bean.getWho());
        WebActivity.startWebActivity(mActivity, bundle);
    }

    @Override
    public void refillDate(List<ResultsBean> list) {
        mRecyclerAdapter.updateItems(list);
    }

    @Override
    public void appendMoreDate(List<ResultsBean> list) {
        mRecyclerAdapter.appendMoreDate(list);
    }

    @Override
    public void showRefreshError(String errorStr) {
        Snackbar.make(mSwipeRefreshLayout, errorStr, Snackbar.LENGTH_LONG)
                .setActionTextColor(App.getAppColor(R.color.Blue))
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.fetchMore();
                    }
                }).show();
    }

    @Override
    public void hasNoMoreDate() {
        super.hasNoMoreDate();
        Snackbar.make(mRecyclerView, R.string.tip_no_more_load, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showLoading() {
        super.showLoading();
        mMultipleStatusView.showLoading();
    }

    @Override
    public void showError() {
        super.showError();
        mMultipleStatusView.showError();
    }

    @Override
    public void showEmpty() {
        super.showEmpty();
        mMultipleStatusView.showEmpty();
    }

    @Override
    public void showContent() {
        super.showContent();
        mMultipleStatusView.showContent();
    }

    @Override
    public void showDisNetWork() {
        super.showDisNetWork();
        mMultipleStatusView.showNoNetwork();
    }

    @Override
    public void hideRefresh() {
        super.hideRefresh();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showRefresh() {
        super.showRefresh();
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
