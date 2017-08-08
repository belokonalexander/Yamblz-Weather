package com.yamblz.voltek.weather.presentation.ui.adapter.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager;
import com.yamblz.voltek.weather.presentation.ui.adapter.delegates.ForecastAdapterDelegate;
import com.yamblz.voltek.weather.presentation.ui.adapter.models.AdapterItem;

import java.util.List;

/**
 * Created on 02.08.2017.
 */

public class ForecastAdapter extends RecyclerView.Adapter {

    private AdapterDelegatesManager<List<? extends AdapterItem>> delegatesManager;
    private List<? extends AdapterItem> items;

    public ForecastAdapter(LayoutInflater inflater, List<? extends AdapterItem> items) {
        this.items = items;

        delegatesManager = new AdapterDelegatesManager<>();

        delegatesManager.addDelegate(new ForecastAdapterDelegate(inflater));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return delegatesManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        delegatesManager.onBindViewHolder(items, position, holder);
    }

    @Override
    public int getItemViewType(int position) {
        return delegatesManager.getItemViewType(items, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void tryUpdateContent(List<? extends AdapterItem> weather) {
        if(items.hashCode()!=weather.hashCode()) {
            items = weather;
            notifyDataSetChanged();
        }
    }
}
