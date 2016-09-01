package com.gank.gankly.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.config.Constants;
import com.gank.gankly.listener.RecyclerOnClick;
import com.gank.gankly.presenter.IBaseRefreshPresenter;
import com.gank.gankly.presenter.impl.AndroidPresenterImpl;
import com.gank.gankly.ui.base.BaseSwipeRefreshLayout;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.web.WebActivity;
import com.gank.gankly.utils.CircularAnimUtil;
import com.gank.gankly.view.IMeiziView;
import com.gank.gankly.widget.MultipleStatusView;
import com.socks.library.KLog;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import butterknife.BindView;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Create by LingYan on 2016-4-26
 * Email:137387869@qq.com
 */
public class AndroidFragment extends LazyFragment implements SwipeRefreshLayout.OnRefreshListener,
        RecyclerOnClick, IMeiziView<List<ResultsBean>> {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;
    @BindView(R.id.swipe_refresh)
    BaseSwipeRefreshLayout mSwipeRefreshLayout;

    private MainActivity mActivity;
    private GankAdapter mGankAdapter;
    private IBaseRefreshPresenter mPresenter;

    public AndroidFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (MainActivity) context;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new AndroidPresenterImpl(mActivity, this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        KLog.d("onCreate");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    protected void initValues() {
        mMultipleStatusView.showLoading();
    }

    @Override
    protected void initViews() {
        mMultipleStatusView.setBackgroundColor(App.getAppColor(R.color.book_recycler_item_bg));
        mGankAdapter = new GankAdapter(mActivity);
        mGankAdapter.setOnItemClickListener(this);
        initRecycler();
    }

    @Override
    protected void bindLister() {
        setMultipleStatusView(mMultipleStatusView);
        mMultipleStatusView.setListener(new MultipleStatusView.OnMultipleClick() {
            @Override
            public void retry(View v) {
                mMultipleStatusView.showLoading();
                mPresenter.fetchNew();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_swiperefresh_multiple_status;
    }

    @Override
    protected void initData() {
        onDownRefresh();
    }

    private void initRecycler() {
        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(mActivity));

//        mRecyclerView.addItemDecoration(new RecycleViewDivider(mActivity, R.drawable.shape_item_divider));
        mSwipeRefreshLayout.setOnScrollListener(new BaseSwipeRefreshLayout.OnSwipeRefRecyclerViewListener() {
            @Override
            public void onRefresh() {
                mPresenter.fetchNew();
            }

            @Override
            public void onLoadMore() {
                mPresenter.fetchMore();
            }
        });

//        mSwipeRefreshLayout.getRecyclerView().setItemAnimator(new ScaleInAnimator());
        mSwipeRefreshLayout.getRecyclerView().setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mGankAdapter);
        alphaAdapter.setFirstOnly(true);
        alphaAdapter.setDuration(500);
        alphaAdapter.setInterpolator(new OvershootInterpolator(.5f));
        mSwipeRefreshLayout.setAdapter(alphaAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(App.getAppColor(R.color.colorPrimary));
    }


    private void onDownRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        fetchDate();
    }

    private void fetchDate() {
        mPresenter.fetchNew();
    }

    public static AndroidFragment newInstance() {
        AndroidFragment fragment = new AndroidFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onRefresh() {
        onDownRefresh();
    }

    @Override
    public void onClick(View view, ResultsBean bean) {
        Bundle bundle = new Bundle();
        bundle.putString("title", bean.getDesc());
        bundle.putString("url", bean.getUrl());
        bundle.putString("type", Constants.ANDROID);
        bundle.putString("author", bean.getWho());
        Intent intent = new Intent(mActivity, WebActivity.class);
        intent.putExtras(bundle);
        CircularAnimUtil.startActivity(mActivity, intent, view, R.color.white_half);
    }

    @Override
    public void refillDate(List<ResultsBean> list) {
        mGankAdapter.updateItems(list);
    }

    @Override
    public void appendMoreDate(List<ResultsBean> list) {
        mGankAdapter.appendMoreDate(list);
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        KLog.d("onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshUi();
    }

    public void refreshUi() {
        if (App.isNight()) {
            mSwipeRefreshLayout.getRecyclerView().setBackgroundResource(R.color.dark_background);
        } else {
            mSwipeRefreshLayout.getRecyclerView().setBackgroundResource(R.color.base_refresh_list_bg);
        }

        TypedValue background = new TypedValue();
        TypedValue textColor = new TypedValue();
        Resources.Theme theme = mActivity.getTheme();
        theme.resolveAttribute(R.attr.baseAdapterItemBackground, background, true);
        theme.resolveAttribute(R.attr.baseAdapterItemTextColor, textColor, true);

        int childCount = mSwipeRefreshLayout.getRecyclerView().getChildCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            CardView childView = (CardView) mSwipeRefreshLayout.getRecyclerView().getChildAt(childIndex);
            childView.setCardBackgroundColor(App.getAppColor(background.resourceId));
//            View infoLayout = childView.findViewById(R.id.info_layout);
//            infoLayout.setBackgroundResource(background.resourceId);
            TextView nickName = (TextView) childView.findViewById(R.id.goods_txt_title);
//            nickName.setBackgroundResource(background.resourceId);
            nickName.setTextColor(App.getAppColor(textColor.resourceId));
//            TextView motto = (TextView) childView.findViewById(R.id.tv_motto);
//            motto.setBackgroundResource(background.resourceId);
//            motto.setTextColor(resources.getColor(textColor.resourceId));
        }

        //让 RecyclerView 缓存在 Pool 中的 Item 失效
        //那么，如果是ListView，要怎么做呢？这里的思路是通过反射拿到 AbsListView 类中的 RecycleBin 对象，然后同样再用反射去调用 clear 方法
        Class<RecyclerView> recyclerViewClass = RecyclerView.class;
        try {
            Field declaredField = recyclerViewClass.getDeclaredField("mRecycler");
            declaredField.setAccessible(true);
            Method declaredMethod = Class.forName(RecyclerView.Recycler.class.getName()).getDeclaredMethod("clear", (Class<?>[]) new Class[0]);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(declaredField.get(mSwipeRefreshLayout.getRecyclerView()), new Object[0]);
            RecyclerView.RecycledViewPool recycledViewPool = mSwipeRefreshLayout.getRecyclerView().getRecycledViewPool();
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

    @Override
    public void onHiddenChanged(boolean hidden) {

        super.onHiddenChanged(hidden);
    }
}
