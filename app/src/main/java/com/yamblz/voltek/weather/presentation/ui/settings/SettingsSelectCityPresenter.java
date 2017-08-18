package com.yamblz.voltek.weather.presentation.ui.settings;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.domain.interactor.CitySettingsInteractor;
import com.yamblz.voltek.weather.presentation.base.BasePresenter;
import com.yamblz.voltek.weather.presentation.ui.adapter.models.CityAdapterItem;
import com.yamblz.voltek.weather.utils.LogUtils;
import com.yamblz.voltek.weather.utils.rx.RxSchedulers;

import java.util.List;

@InjectViewState
public class SettingsSelectCityPresenter extends BasePresenter<SettingsCityView> {

    private final CitySettingsInteractor interactor;
    private final RxSchedulers rxSchedulers;

    public SettingsSelectCityPresenter(CitySettingsInteractor interactor, RxSchedulers rxSchedulers) {
        this.interactor = interactor;
        this.rxSchedulers = rxSchedulers;
    }

    public void clearTextView() {
        getViewState().clearText();
    }

    public void findSuggestions(String text) {
        text = text.trim();
        if (text.length() > 0) {
            interactor.getCitySuggestions(text)
                    .compose(rxSchedulers.getIOToMainTransformerSingle())
                    .subscribe(this::onNextSuggestions, this::onError);
        }
    }

    private void onError(Throwable throwable) {
        LogUtils.log("error: ", throwable);
        getViewState().showError(throwable);
    }

    private void onNextSuggestions(List<CityAdapterItem> result) {
        getViewState().showSuggestions(result);
    }

    /**
     * if we change city with id - save directly to prefs
     *
     * @param city the chosen city
     */
    public void selectCity(@NonNull CityUIModel city) {
        interactor.saveCity(city)
                .compose(rxSchedulers.getIOToMainTransformerSingle())
                .subscribe(this::onSuccessCitySaved, this::onError);
    }

    private void onSuccessCitySaved(CityUIModel city) {
        getViewState().selectCity(city);
    }

    /**
     * if we put random name - should check it with db/api and save it to shared prefs
     *
     * @param cityName the city name we want to select
     */
    public void selectCity(@NonNull String cityName) {

        if (cityName.trim().length() > 0) {
            interactor.saveCity(new CityUIModel(cityName))
                    .compose(rxSchedulers.getIOToMainTransformerSingle())
                    .subscribe(this::onSuccessCitySaved, this::onError);
        }

    }


}
