package com.yamblz.voltek.weather.presentation.base.moxyprimitives;

/**
 * Created on 05.08.2017.
 */

public class LinkableBoolean {

    private boolean value;

    public LinkableBoolean(boolean value) {
        this.value = value;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
