package com.yamblz.voltek.weather.presentation.ui.menu.items;

import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;

/**
 * Created on 03.08.2017.
 */

public class WeatherItem extends SecondaryDrawerItem {

    private CityUIModel model;

    public WeatherItem(CityUIModel model) {
        this.model = model;
    }

    @Override
    public long getIdentifier() {
        return model.id;
    }

    public CityUIModel getModel() {
        return model;
    }


}
