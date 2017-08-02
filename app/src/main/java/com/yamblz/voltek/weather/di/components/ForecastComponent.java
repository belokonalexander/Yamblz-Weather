package com.yamblz.voltek.weather.di.components;

import com.yamblz.voltek.weather.di.modules.ForecastModule;
import com.yamblz.voltek.weather.di.scopes.ForecastScope;
import com.yamblz.voltek.weather.presentation.ui.forecast.ForecastPresenter;

import dagger.Subcomponent;

/**
 * Created on 31.07.2017.
 */
@Subcomponent(modules = ForecastModule.class)
@ForecastScope
public interface ForecastComponent {

    //void inject(ForecastFragment forecastFragment);
    ForecastPresenter getForecastPresenter();
}
