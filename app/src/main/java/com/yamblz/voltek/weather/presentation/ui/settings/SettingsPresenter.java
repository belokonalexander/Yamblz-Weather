package com.yamblz.voltek.weather.presentation.ui.settings;

import com.arellomobile.mvp.InjectViewState;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.domain.interactor.SettingsInteractor;
import com.yamblz.voltek.weather.presentation.base.BasePresenter;
import com.yamblz.voltek.weather.utils.rx.RxSchedulers;

/**
 * Created on 02.08.2017.
 */

@InjectViewState
public class SettingsPresenter extends BasePresenter<SettingsView> {

    private SettingsInteractor interactor;
    private RxSchedulers rxSchedulers;

    public SettingsPresenter(SettingsInteractor settingsInteractor, RxSchedulers rxSchedulers) {
        this.interactor = settingsInteractor;
        this.rxSchedulers = rxSchedulers;
    }


    private void loadSettingsData() {
        interactor.getCurrentCity()
                .compose(rxSchedulers.getIOToMainTransformerSingle())
                .subscribe(this::successLoadSettings, this::onError);
    }

    private void onError(Throwable throwable) {

    }

    private void successLoadSettings(CityUIModel cityUIModel) {
        getViewState().setCitySummary(cityUIModel.name);
    }

    @Override
    public void attachView(SettingsView view) {
        super.attachView(view);
        loadSettingsData();

    }
}
