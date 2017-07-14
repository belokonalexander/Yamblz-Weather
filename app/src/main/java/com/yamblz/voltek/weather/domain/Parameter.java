package com.yamblz.voltek.weather.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Parameter<T> {

    @Nullable
    private String flag = null;
    @Nullable
    private T item = null;

    public Parameter() {}

    public Parameter(@NonNull T item) {
        this.item = item;
    }

    public Parameter(@NonNull String flag) {
        this.flag = flag;
    }

    public Parameter(@NonNull String flag, @NonNull T item) {
        this.flag = flag;
        this.item = item;
    }

    @Nullable
    public String getFlag() {
        return flag;
    }

    @Nullable
    public T getItem() {
        return item;
    }
}
