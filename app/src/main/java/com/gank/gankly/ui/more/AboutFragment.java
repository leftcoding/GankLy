package com.gank.gankly.ui.more;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.ui.base.BaseFragment;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 关于
 * Create by LingYan on 2016-05-10
 * Email:137387869@qq.com
 */
public class AboutFragment extends BaseFragment {
    @BindView(R.id.about_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.about_collapsing)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindString(R.string.about_gank_url)
    String mGankUrl;
    @BindString(R.string.about_mzitu_url)
    String mMzituUrl;
    @BindString(R.string.about_my_github_url)
    String mGithubUrl;

    private MoreActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MoreActivity) context;
    }

    @Override
    protected void initValues() {
    }

    @Override
    protected void initViews() {
        mCollapsingToolbarLayout.setTitle(App.getAppString(R.string.navigation_about));
        mActivity.setSupportActionBar(mToolbar);
        ActionBar bar = mActivity.getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(v -> mActivity.onBackPressed());
    }

    @Override
    protected void bindListener() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about;
    }

    @OnClick(R.id.about_item_text_gank)
    void clickGank() {
        gotoUrl(mGankUrl);
    }

    @OnClick(R.id.about_item_text_mzitu)
    void clickMzitu() {
        gotoUrl(mMzituUrl);
    }

    @OnClick(R.id.about_item_text_github)
    void clickGithub() {
        gotoUrl(mGithubUrl);
    }

    private void gotoUrl(String url) {
        if (url == null || !url.startsWith("http")) {
            throw new RuntimeException("not complete web site");
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
