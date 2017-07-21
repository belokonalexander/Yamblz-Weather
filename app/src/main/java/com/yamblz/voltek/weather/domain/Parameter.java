package com.yamblz.voltek.weather.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Parameter<T> {

    @Nullable
    private String flag = null;
    @Nullable
    private T item = null;

    public Parameter() {}

    @Nullable
    public String getFlag() {
        return flag;
    }

    @Nullable
    public T getItem() {
        return item;
    }

    public void setFlag(@NonNull String flag) {
        this.flag = flag;
    }

    public void setItem(@NonNull T item) {
        this.item = item;
    }
}
