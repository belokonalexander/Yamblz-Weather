package com.yamblz.voltek.weather.presentation.ui.menu.items;

import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;

/**
 * Created on 03.08.2017.
 */

public class WeatherItem extends PrimaryDrawerItem {

    private CityUIModel model;

    public WeatherItem(CityUIModel model) {
        this.model = model;
    }

    public CityUIModel getModel() {
        return model;
    }
}
