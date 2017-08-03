package com.yamblz.voltek.weather.presentation.ui.favorites;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.presentation.base.BaseView;

import java.util.List;

/**
 * Created on 03.08.2017.
 */

public interface FavoritesView extends BaseView {

    @StateStrategyType(value = AddToEndSingleStrategy.class)
    void setFavoritesItems(List<CityUIModel> models);

}
