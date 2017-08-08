package com.yamblz.voltek.weather.presentation.ui.adapter.models;

import com.yamblz.voltek.weather.domain.entity.WeatherUIModel;

import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created on 02.08.2017.
 */

public class ForecastAdapterItem implements AdapterItem {

    @NotNull
    private WeatherUIModel content;

    public ForecastAdapterItem(@NotNull WeatherUIModel content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ForecastAdapterItem{" +
                "content=" + content +
                '}';
    }

    public WeatherUIModel getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ForecastAdapterItem that = (ForecastAdapterItem) o;

        return content.equals(that.content);

    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }
}
