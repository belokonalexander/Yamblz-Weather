package com.yamblz.voltek.weather.presentation.base;

import com.arellomobile.mvp.MvpPresenter;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BasePresenter<V extends BaseView> extends MvpPresenter<V> {

    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void attachView(V view) {
        super.attachView(view);
        getViewState().attachInputListeners();
    }

    @Override
    public void detachView(V view) {
        getViewState().detachInputListeners();
        super.detachView(view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

}
