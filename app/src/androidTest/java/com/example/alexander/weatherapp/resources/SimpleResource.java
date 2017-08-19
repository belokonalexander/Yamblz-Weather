package com.example.alexander.weatherapp.resources;

import android.support.test.espresso.IdlingResource;
import android.view.View;

/**
 * Created on 19.08.2017.
 */

public abstract class SimpleResource<T extends View> implements IdlingResource {

    private T view;
    private ResourceCallback callback;

    SimpleResource(T view) {
        this.view = view;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isIdleNow() {
        boolean idle = isIdle();
        if (idle) callback.onTransitionToIdle();
        return idle;
    }

    public abstract boolean isIdle();

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.callback = callback;
    }

    T getView() {
        return view;
    }
}
