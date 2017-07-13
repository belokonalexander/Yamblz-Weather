package com.yamblz.voltek.weather.domain;

public class Parameter<T> {

    private String flag;
    private T item;

    public Parameter() {
        this.flag = null;
        this.item = null;
    }

    public Parameter(T item) {
        this.flag = null;
        this.item = item;
    }

    public Parameter(String flag) {
        this.flag = flag;
        this.item = null;
    }

    public Parameter(String flag, T item) {
        this.flag = flag;
        this.item = item;
    }

    public String getFlag() {
        return flag;
    }

    public T getItem() {
        return item;
    }
}
