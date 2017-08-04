package com.yamblz.voltek.weather.presentation.ui.main;

import android.support.v4.app.Fragment;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.domain.exception.DeleteLastCityException;
import com.yamblz.voltek.weather.presentation.base.BaseView;
import com.yamblz.voltek.weather.presentation.base.OnResultCallback;
import com.yamblz.voltek.weather.utils.classes.SetWithSelection;

/**
 * Created on 03.08.2017.
 */

public interface WeatherView extends BaseView {

    @StateStrategyType(value = AddToEndSingleStrategy.class)
    void setFavoritesItems(SetWithSelection<CityUIModel> models);

    @StateStrategyType(value = SkipStrategy.class)
    void navigateTo(Class<? extends Fragment> where, boolean asRoot, OnResultCallback onResultCallback);

    @StateStrategyType(value = SkipStrategy.class)
    void showDialogForCity(CityUIModel selectedCity);

    @StateStrategyType(value = SkipStrategy.class)
    void showError(DeleteLastCityException e);
}
