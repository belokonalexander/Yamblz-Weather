package com.yamblz.voltek.weather.presentation.base;

import com.arellomobile.mvp.MvpPresenter;

public abstract class BasePresenter<V extends BaseView> extends MvpPresenter<V> {

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
}
