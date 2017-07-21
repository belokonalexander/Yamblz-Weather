package com.yamblz.voltek.weather.presentation.ui.settings;

import com.arellomobile.mvp.InjectViewState;
import com.yamblz.voltek.weather.domain.Parameter;
import com.yamblz.voltek.weather.domain.Result;
import com.yamblz.voltek.weather.domain.interactor.SettingsCityInteractor;
import com.yamblz.voltek.weather.presentation.base.BasePresenter;

import java.util.List;

@InjectViewState
public class SettingsPresenter extends BasePresenter<SettingsView> {

    private SettingsCityInteractor settingsCityInteractor;

    SettingsPresenter(SettingsCityInteractor settingsCityInteractor) {
        this.settingsCityInteractor = settingsCityInteractor;
    }

    public void findSuggestions(String text){
        Parameter<String> param = new Parameter<>();
        param.setItem(text);
        settingsCityInteractor.execute(param, this::onNext,this::onError,this::onComplete);
    }

    private void onComplete() {

    }

    private void onError(Throwable throwable) {

    }

    private void onNext(Result<List<String>> result){
        getViewState().showSuggestions(result.getData());
    }
}
