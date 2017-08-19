package com.yamblz.voltek.weather.utils.classes;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created on 03.08.2017.
 */

public class SetWithSelection<T> implements Iterable<T> {

    private Set<T> items = new HashSet<>();

    @Nullable
    private T selectedItem;

    public SetWithSelection(Set<T> items, @Nullable T selectedItem) {
        this.items = items;
        //selected item should be in this set
        this.items.add(selectedItem);
        this.selectedItem = selectedItem;
    }

    @Override
    public String toString() {
        return "SetWithSelection{" +
                "items=" + items +
                ", selectedItem=" + selectedItem +
                '}';
    }

    public SetWithSelection() {
    }

    public Set<T> getItems() {
        return items;
    }

    @Nullable
    public T getSelectedItem() {
        return selectedItem;
    }

    public void addAsSelected(T selectedItem) {
        items.add(selectedItem);
        this.selectedItem = selectedItem;
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return items.iterator();
    }

    public void delete(T removedItem) {
        items.remove(removedItem);
        if (removedItem.equals(selectedItem)) {
            selectedItem = null;
        }
    }

    public void deleteAndSetSelected(T removedItem, T selectedITem) {
        delete(removedItem);
        addAsSelected(selectedITem);
    }


}
