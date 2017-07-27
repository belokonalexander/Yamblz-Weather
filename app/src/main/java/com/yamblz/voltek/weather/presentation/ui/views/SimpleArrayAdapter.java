package com.yamblz.voltek.weather.presentation.ui.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class SimpleArrayAdapter<T> extends ArrayAdapter<T> implements Filterable {

    private List<T> list;

    public SimpleArrayAdapter(Context context, int textViewResourceId, List<T> list) {
        super(context, textViewResourceId);
        this.list = new ArrayList<>(list);
    }

    @Override
    public void add(T object) {
        list.add(object);
        notifyDataSetChanged();
    }

    @Override
    public void addAll(@NonNull Collection<? extends T> collection) {
        list.addAll(collection);
        notifyDataSetChanged();
    }


    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }


}