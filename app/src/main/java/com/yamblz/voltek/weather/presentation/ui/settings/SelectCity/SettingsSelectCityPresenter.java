package com.yamblz.voltek.weather.presentation.ui.settings.SelectCity;

import com.arellomobile.mvp.InjectViewState;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.domain.interactor.SettingsInteractor;
import com.yamblz.voltek.weather.presentation.base.BasePresenter;
import com.yamblz.voltek.weather.presentation.ui.adapter.models.CityAdapterItem;
import com.yamblz.voltek.weather.utils.rx.ListMapper;
import com.yamblz.voltek.weather.utils.rx.RxSchedulers;

import java.util.List;

import io.reactivex.functions.Function;

@InjectViewState
public class SettingsSelectCityPresenter extends BasePresenter<SettingsCityView> {

    private SettingsInteractor interactor;
    private RxSchedulers rxSchedulers;

    public SettingsSelectCityPresenter(SettingsInteractor interactor, RxSchedulers rxSchedulers) {
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
                    .map(mapCityToAdapterITem())
                    .compose(rxSchedulers.getIOToMainTransformerSingle())
                    .subscribe(this::onNextSuggestions, this::onError);
        }
    }

    private Function<List<CityUIModel>, List<CityAdapterItem>> mapCityToAdapterITem() {
        return ListMapper.mapList(CityAdapterItem::new);
    }


    private void onError(Throwable throwable) {
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
    public void selectCity(CityUIModel city) {
        interactor.saveCity(city)
                .compose(rxSchedulers.getIOToMainTransformerSingle())
                .subscribe(this::onSuccessCitySaved, this::onError);
    }

    public void onSuccessCitySaved(CityUIModel city) {
        getViewState().selectCity(city);
    }

    /**
     * if we put random name - should check it with db/api and save it to shared prefs
     *
     * @param cityName the city name we want to select
     */
    public void selectCity(String cityName) {

        if (cityName.trim().length() > 0) {
            interactor.saveCity(new CityUIModel(cityName))
                    .compose(rxSchedulers.getIOToMainTransformerSingle())
                    .subscribe(this::onSuccessCitySaved, this::onError);
        }

    }


}
