package com.gank.gankly.ui.main.meizi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.bean.DailyMeiziBean;
import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.config.ViewsModel;
import com.gank.gankly.listener.ItemClick;
import com.gank.gankly.presenter.DailyMeiziPresenter;
import com.gank.gankly.presenter.impl.DailyMeiziPresenterImpl;
import com.gank.gankly.ui.base.BaseSwipeRefreshLayout;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.browse.BrowseActivity;
import com.gank.gankly.ui.main.MainActivity;
import com.gank.gankly.view.IDailyMeiziView;
import com.gank.gankly.widget.MultipleStatusView;
import com.gank.gankly.widget.RecycleViewDivider;

import java.util.List;

import butterknife.Bind;

/**
 * Create by LingYan on 2016-07-01
 */
public class DailyMeiziFragment extends LazyFragment implements IDailyMeiziView<List<DailyMeiziBean>>,
        ItemClick {
    private DailyMeiziPresenter mPresenter;
    private MainActivity mActivity;

    @Bind(R.id.loading_view)
    MultipleStatusView mMultipleStatusView;
    @Bind(R.id.meizi_swipe_refresh)
    BaseSwipeRefreshLayout mSwipeRefreshLayout;
    DailyMeiziAdapter mDailyMeiziAdapter;

    private static DailyMeiziFragment sDailyMeiziFragment;
    private ProgressDialog mDialog;

    public DailyMeiziFragment() {
    }

    public static DailyMeiziFragment getInstance() {
        if (sDailyMeiziFragment == null) {
            synchronized (DailyMeiziFragment.class) {
                if (sDailyMeiziFragment == null) {
                    sDailyMeiziFragment = new DailyMeiziFragment();
                }
            }
        }
        return sDailyMeiziFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new DailyMeiziPresenterImpl(mActivity, this);
    }

    @Override
    protected void initValues() {
    }

    @Override
    protected void initViews() {
        mDailyMeiziAdapter = new DailyMeiziAdapter();
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(mActivity));
        mSwipeRefreshLayout.setColorSchemeColors(App.getAppColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setRefreshListener(new BaseSwipeRefreshLayout.OnSwipeRefRecyclerViewListener() {
            @Override
            public void onRefresh() {
                mPresenter.fetchNew();
            }

            @Override
            public void onLoadMore() {

            }
        });
        mSwipeRefreshLayout.setAdapter(mDailyMeiziAdapter);
        mSwipeRefreshLayout.getRecyclerView().setItemAnimator(new DefaultItemAnimator());
        mSwipeRefreshLayout.getRecyclerView().addItemDecoration(new RecycleViewDivider(mActivity, R.drawable.shape_item_divider));
        mDailyMeiziAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void bindLister() {

    }

    @Override
    protected void initDate() {
        mMultipleStatusView.showLoading();
        mPresenter.fetchNew();
    }

    private void createDialog() {
        if (mDialog == null) {
            mDialog = new ProgressDialog(mActivity);
        }
        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDialog.setMessage(App.getAppString(R.string.loading_meizi_images));
        mDialog.setIndeterminate(false);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setProgress(0);
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mPresenter.unSubscribe();
            }
        });
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    @Override
    public void disDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void setProgressValue(int value) {
        if (mDialog != null) {
            mDialog.setProgress(value);
        }
    }

    @Override
    public void setMax(int value) {
        if (mDialog != null) {
            mDialog.setMax(value);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gift;
    }

    @Override
    public void refillDate(List<DailyMeiziBean> list) {
        mDailyMeiziAdapter.updateItem(list);
    }

    @Override
    public void gotoBrowseActivity() {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(mActivity, BrowseActivity.class);
        bundle.putString(ViewsModel.Gift, ViewsModel.Daily);
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
    }

    @Override
    public void hideRefresh() {
        super.hideRefresh();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showContent() {
        super.showContent();
        mMultipleStatusView.showContent();
    }

    @Override
    public void onClick(int position, Object object) {
        DailyMeiziBean dailyMeiziBean = (DailyMeiziBean) object;
        String url = dailyMeiziBean.getUrl();
        if (!TextUtils.isEmpty(url)) {
            createDialog();
            mPresenter.fetchImageUrls(url);
        }
    }

    public List<GiftBean> getList() {
        return mPresenter.getList();
    }
}
