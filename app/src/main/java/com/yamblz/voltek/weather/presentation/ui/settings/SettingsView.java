package com.yamblz.voltek.weather.presentation.ui.settings;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.presentation.base.BaseView;
import com.yamblz.voltek.weather.presentation.base.strategy.AddToEndSingleStrategyByTag;

import java.util.List;

public interface SettingsView extends BaseView {

    @StateStrategyType(value = AddToEndSingleStrategyByTag.class, tag = "settingsState")
    void showSuggestions(List<CityUIModel> strings);

    @StateStrategyType(value = AddToEndSingleStrategyByTag.class, tag = "settingsState")
    void setCity(String cityName);

    @StateStrategyType(value = SkipStrategy.class)
    void showError(@NonNull Throwable error);

}
