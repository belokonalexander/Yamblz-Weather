package com.yamblz.voltek.weather.presentation.ui.settings;

import com.arellomobile.mvp.InjectViewState;
import com.yamblz.voltek.weather.domain.Parameter;
import com.yamblz.voltek.weather.domain.Result;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.domain.interactor.CurrentSettingsInteractor;
import com.yamblz.voltek.weather.domain.interactor.SettingsCitySuggestionsInteractor;
import com.yamblz.voltek.weather.domain.interactor.SettingsSetCityInteractor;
import com.yamblz.voltek.weather.presentation.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

@InjectViewState
public class SettingsPresenter extends BasePresenter<SettingsView> {

    private SettingsCitySuggestionsInteractor settingsCitySuggestionsInteractor;
    private SettingsSetCityInteractor settingsSetCityInteractor;
    private CurrentSettingsInteractor currentSettingsInteractor;

    public SettingsPresenter(SettingsCitySuggestionsInteractor settingsCitySuggestionsInteractor,
                      SettingsSetCityInteractor settingsSetCityInteractor,
                      CurrentSettingsInteractor currentSettingsInteractor
    ) {
        this.settingsCitySuggestionsInteractor = settingsCitySuggestionsInteractor;
        this.settingsSetCityInteractor = settingsSetCityInteractor;
        this.currentSettingsInteractor = currentSettingsInteractor;
        this.loadSettingsData();

    }

    private void loadSettingsData() {
        currentSettingsInteractor.execute(new Parameter<>(), this::successLoadSettings, this::onError, this::onComplete);
    }

    private void successLoadSettings(Result<CityUIModel> city) {
        if (city.getData() != null)
            getViewState().setCity(city.getData().name);
    }

    public void findSuggestions(String text) {
        if (text.length() > 0) {
            Parameter<String> param = new Parameter<>();
            param.setItem(text);
            settingsCitySuggestionsInteractor.unsubscribe();
            settingsCitySuggestionsInteractor.execute(param, this::onNextSuggestions, this::onError, this::onComplete);
        } else {
            getViewState().showSuggestions(new ArrayList<>());
        }
    }


    private void onError(Throwable throwable) {
        getViewState().showError(throwable);
    }

    private void onNextSuggestions(Result<List<CityUIModel>> result) {
        getViewState().showSuggestions(result.getData());
    }

    /**
     * if we change city with id - save directly to prefs
     *
     * @param city the chosen city
     */
    public void selectCity(CityUIModel city) {
        getViewState().setCity(city.name);
        Parameter<CityUIModel> param = new Parameter<>();
        param.setItem(city);
        settingsSetCityInteractor.execute(param, this::onNext, this::onError, this::onComplete);
    }

    /**
     * if we put random name - should check it with db/api and save it to shared prefs
     *
     * @param cityName the city name we want to select
     */
    public void selectCity(String cityName) {
        Parameter<CityUIModel> param = new Parameter<>();
        param.setItem(new CityUIModel(cityName));
        settingsSetCityInteractor.execute(param, this::onNextCorrectCity, this::onError, this::onComplete);
    }

    private void onNextCorrectCity(Result<CityUIModel> cityUIModelResult) {
        if (cityUIModelResult.getData() != null)
            getViewState().setCity(cityUIModelResult.getData().name);
    }

}
