package com.yamblz.voltek.weather.presentation.ui.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.domain.exception.DeleteLastCityException;
import com.yamblz.voltek.weather.presentation.base.BaseView;
import com.yamblz.voltek.weather.presentation.base.strategy.AddToEndSingleStrategyByTag;
import com.yamblz.voltek.weather.presentation.ui.menu.items.WeatherItem;
import com.yamblz.voltek.weather.utils.classes.SetWithSelection;

/**
 * Created on 03.08.2017.
 */

public interface WeatherView extends BaseView {

    @StateStrategyType(value = AddToEndSingleStrategy.class)
    void setFavoritesItems(SetWithSelection<CityUIModel> models);

    @StateStrategyType(value = AddToEndSingleStrategyByTag.class, tag = "ITEM_SELECTION")
    void selectWeatherItem(WeatherItem item);

    @StateStrategyType(value = AddToEndSingleStrategyByTag.class, tag = "ITEM_SELECTION")
    void selectStickyItem();

    @StateStrategyType(value = SkipStrategy.class)
    void deleteWeatherItem(WeatherItem item);

    @StateStrategyType(value = SkipStrategy.class)
    void navigateTo(Class<? extends Fragment> where, boolean asRoot, String tag, Bundle bundle);

    @StateStrategyType(value = SkipStrategy.class)
    void showDialogForCity(CityUIModel selectedCity);

    @StateStrategyType(value = SkipStrategy.class)
    void showError(DeleteLastCityException e);

    @StateStrategyType(value = SkipStrategy.class)
    void scrollToElement(WeatherItem item);
}
