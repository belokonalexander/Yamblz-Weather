package com.yamblz.voltek.weather.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Result<T> {

    @Nullable
    private String message = null;
    @Nullable
    private T data = null;

    public Result() {}

    public Result(@NonNull T data) {
        this.data = data;
    }

    public Result(@NonNull String message) {
        this.message = message;
    }

    public Result(@NonNull String message, @NonNull T data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
