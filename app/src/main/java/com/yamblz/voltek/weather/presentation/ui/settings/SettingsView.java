package com.yamblz.voltek.weather.presentation.ui.settings;

import com.yamblz.voltek.weather.domain.Result;
import com.yamblz.voltek.weather.presentation.base.BaseView;

import java.util.List;

public interface SettingsView extends BaseView {

    void showSuggestions(List<String> strings);
}
