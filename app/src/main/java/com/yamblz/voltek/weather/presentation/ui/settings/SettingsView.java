package com.yamblz.voltek.weather.presentation.ui.settings;

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.yamblz.voltek.weather.presentation.base.BaseView;

/**
 * Created on 07.08.2017.
 */

@StateStrategyType(value = SkipStrategy.class)
public interface SettingsView extends BaseView {

    void jobStateChanged();

}
