package com.yamblz.voltek.weather.presentation.ui.settings;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.presentation.base.BaseView;
import com.yamblz.voltek.weather.presentation.ui.adapter.models.CityAdapterItem;

import java.util.List;

public interface SettingsCityView extends BaseView {

    @StateStrategyType(value = AddToEndSingleStrategy.class)
    void showSuggestions(List<CityAdapterItem> strings);

    @StateStrategyType(value = SkipStrategy.class)
    void selectCity(CityUIModel city);

    @StateStrategyType(value = SkipStrategy.class)
    void showError(@NonNull Throwable error);

    @StateStrategyType(value = SkipStrategy.class)
    void clearText();

}
