package com.yamblz.voltek.weather.domain;

public class Result<T> {

    private String message;
    private T data;

    public Result() {
        this.message = null;
        this.data = null;
    }

    public Result(T data) {
        this.message = null;
        this.data = data;
    }

    public Result(String message) {
        this.message = message;
        this.data = null;
    }

    public Result(String message, T data) {
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
