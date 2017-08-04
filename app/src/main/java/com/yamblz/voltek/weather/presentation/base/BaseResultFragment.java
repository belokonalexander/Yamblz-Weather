package com.yamblz.voltek.weather.presentation.base;

/**
 * Created on 04.08.2017.
 */

public abstract class BaseResultFragment extends BaseFragment {

    protected OnResultCallback citySelectedListener;

    public void setResultCallback(OnResultCallback listener) {
        this.citySelectedListener = listener;
    }

}
