package com.example.alexander.weatherapp.resources;

import android.support.v7.widget.RecyclerView;

/**
 * Created on 18.08.2017.
 */

public class RecyclerIsNotEmptyResource extends SimpleResource<RecyclerView> {

    private int startSize;

    public RecyclerIsNotEmptyResource(RecyclerView view) {
        super(view);
        this.startSize = getView().getAdapter().getItemCount();
    }

    @Override
    public String getName() {
        return RecyclerIsNotEmptyResource.class.getName();
    }

    @Override
    public boolean isIdle() {
        return getView().getAdapter().getItemCount() > startSize;
    }


}
