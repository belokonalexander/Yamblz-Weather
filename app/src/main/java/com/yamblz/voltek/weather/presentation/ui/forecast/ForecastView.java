package com.yamblz.voltek.weather.presentation.ui.forecast;

import android.support.annotation.Nullable;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.yamblz.voltek.weather.domain.entity.WeatherUIModel;
import com.yamblz.voltek.weather.presentation.base.BaseView;

import java.util.List;

public interface ForecastView extends BaseView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showLoading(boolean show);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showData(@Nullable List<WeatherUIModel> weather);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showError(@Nullable Throwable error);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void initTitle();
}
