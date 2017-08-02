package com.yamblz.voltek.weather.presentation.ui.adapter.delegates;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.yamblz.voltek.weather.R;
import com.yamblz.voltek.weather.presentation.ui.adapter.adapters.OnAdapterItemClickListener;
import com.yamblz.voltek.weather.presentation.ui.adapter.models.AdapterItem;
import com.yamblz.voltek.weather.presentation.ui.adapter.models.CityAdapterItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 02.08.2017.
 */

public class CityAdapterDelegate<T extends AdapterItem> extends AdapterDelegate<List<T>> {

    private LayoutInflater inflater;
    private OnAdapterItemClickListener clickListener;

    public CityAdapterDelegate(LayoutInflater inflater, OnAdapterItemClickListener clickListener) {
        this.inflater = inflater;
        this.clickListener = clickListener;
    }

    @Override
    protected boolean isForViewType(@NonNull List<T> items, int position) {
        return items.get(position) instanceof CityAdapterItem;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new CityViewHolder(inflater.inflate(R.layout.item_city, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<T> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        ((CityViewHolder) holder).bindTo((CityAdapterItem) items.get(position), clickListener);
    }

    static class CityViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.city_name)
        TextView cityName;

        public CityViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindTo(CityAdapterItem displayableItem, OnAdapterItemClickListener clickListener) {
            cityName.setText(displayableItem.getCityName());
            cityName.setOnClickListener(v -> clickListener.onClick(displayableItem));
        }
    }

}
