package com.yamblz.voltek.weather.presentation.ui.adapter.models;

import com.yamblz.voltek.weather.domain.entity.CityUIModel;

/**
 * Created on 02.08.2017.
 */

public class CityAdapterItem implements AdapterItem {

    private final CityUIModel content;

    public CityAdapterItem(CityUIModel content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CityAdapterItem{" +
                "content=" + content +
                '}';
    }

    public CityUIModel getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CityAdapterItem that = (CityAdapterItem) o;

        return content.equals(that.content);

    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }
}
