package com.yamblz.voltek.weather.presentation.ui.settings;

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.yamblz.voltek.weather.presentation.base.BaseView;

public interface SettingsView extends BaseView {

    @StateStrategyType(value = SkipStrategy.class)
    void setCitySummary(String city);

}
