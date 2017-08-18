package com.yamblz.voltek.weather.presentation.ui.menu.items;

import android.support.annotation.IdRes;

import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

/**
 * Created on 03.08.2017.
 */

public class MainDrawerItem extends PrimaryDrawerItem {

    @IdRes
    private final int itemId;

    public MainDrawerItem(int itemId) {
        this.itemId = itemId;
    }

    public int getItemId() {
        return itemId;
    }
}
