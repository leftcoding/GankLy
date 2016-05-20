package com.gank.gankly.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gank.gankly.ui.presenter.BasePresenter;

import butterknife.ButterKnife;


/**
 * Create by LingYan on 2016-04-05
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment {
    protected P mPresenter;

//    private CompositeSubscription mCompositeSubscription;
//
//
//    public CompositeSubscription getCompositeSubscription() {
//        if (this.mCompositeSubscription == null) {
//            this.mCompositeSubscription = new CompositeSubscription();
//        }
//
//        return this.mCompositeSubscription;
//    }


//    public void addSubscription(Subscription s) {
//        CompositeSubscription mCompositeSubscription = new CompositeSubscription();
//        mCompositeSubscription.add(s);
//    }

//    public void unSubscribe() {
//        if (this.mCompositeSubscription != null) {
//    KLog.d("unsubscribe");
//    this.mCompositeSubscription.unsubscribe();
//}

@Override
public void onAttach(Context context){
        super.onAttach(context);
        }

@Nullable
@Override
public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(getLayoutId(),container,false);
        }

@Override
public void onViewCreated(View view,@Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        ButterKnife.bind(this,view);
        initValues();
        initViews();
        bindLister();
        }

@Override
public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        }

protected abstract void initValues();

protected abstract void initViews();

protected abstract void bindLister();

protected abstract int getLayoutId();

@Override
public void onDestroyView(){
        super.onDestroyView();
        ButterKnife.unbind(this);
//        unSubscribe();
        }
        }
