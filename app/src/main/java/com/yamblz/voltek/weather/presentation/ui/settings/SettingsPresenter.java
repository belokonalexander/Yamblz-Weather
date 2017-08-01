package com.yamblz.voltek.weather.presentation.ui.settings;

import com.arellomobile.mvp.InjectViewState;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.domain.interactor.SettingsInteractor;
import com.yamblz.voltek.weather.presentation.base.BasePresenter;
import com.yamblz.voltek.weather.utils.rx.RxSchedulers;

import java.util.ArrayList;
import java.util.List;

@InjectViewState
public class SettingsPresenter extends BasePresenter<SettingsView> {

    private SettingsInteractor interactor;
    private RxSchedulers rxSchedulers;

    public SettingsPresenter(SettingsInteractor interactor, RxSchedulers rxSchedulers) {
        this.interactor = interactor;
        this.rxSchedulers = rxSchedulers;
        this.loadSettingsData();
    }

    private void loadSettingsData() {
        interactor.getCurrentCity()
                .compose(rxSchedulers.getIOToMainTransformerSingle())
                .subscribe(this::successLoadSettings, this::onError);

    }

    private void successLoadSettings(CityUIModel city) {
        getViewState().setCity(city.name);
    }

    public void findSuggestions(String text) {
        if (text.length() > 0) {
            interactor.getCitySuggestions(text)
                    .compose(rxSchedulers.getIOToMainTransformerSingle())
                    .subscribe(this::onNextSuggestions, this::onError);

        } else {
            getViewState().showSuggestions(new ArrayList<>());
        }
    }


    private void onError(Throwable throwable) {
        getViewState().showError(throwable);
    }

    private void onNextSuggestions(List<CityUIModel> result) {
        getViewState().showSuggestions(result);
    }

    /**
     * if we change city with id - save directly to prefs
     *
     * @param city the chosen city
     */
    public void selectCity(CityUIModel city) {
        getViewState().setCity(city.name);

        interactor.saveCity(city)
                .compose(rxSchedulers.getIOToMainTransformerSingle())
                .subscribe(this::onSuccessCitySaved, this::onError);

    }

    public void onSuccessCitySaved(CityUIModel city) {
        //TODO
    }

    /**
     * if we put random name - should check it with db/api and save it to shared prefs
     *
     * @param cityName the city name we want to select
     */
    public void selectCity(String cityName) {

        interactor.saveCity(new CityUIModel(cityName))
                .compose(rxSchedulers.getIOToMainTransformerSingle())
                .subscribe(this::onCorrectCityChanged, this::onError);

    }

    private void onCorrectCityChanged(CityUIModel cityUIModel) {
        getViewState().setCity(cityUIModel.name);
    }

}
