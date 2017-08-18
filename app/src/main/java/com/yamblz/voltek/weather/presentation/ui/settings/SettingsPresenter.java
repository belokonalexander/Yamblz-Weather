package com.yamblz.voltek.weather.presentation.ui.settings;

import com.arellomobile.mvp.InjectViewState;
import com.yamblz.voltek.weather.domain.interactor.SettingsInteractor;
import com.yamblz.voltek.weather.presentation.base.BasePresenter;
import com.yamblz.voltek.weather.utils.LogUtils;
import com.yamblz.voltek.weather.utils.rx.RxSchedulers;

import io.reactivex.disposables.Disposable;

/**
 * Created on 07.08.2017.
 */

@InjectViewState
public class SettingsPresenter extends BasePresenter<SettingsView> {

    private final SettingsInteractor settingsInteractor;
    private final RxSchedulers rxSchedulers;

    public SettingsPresenter(SettingsInteractor settingsInteractor, RxSchedulers rxSchedulers) {
        this.settingsInteractor = settingsInteractor;
        this.rxSchedulers = rxSchedulers;
    }

    public void updateWeatherJob() {
        Disposable d = settingsInteractor.updateJob()
                .compose(rxSchedulers.getIOToMainTransformerCompletable())
                .subscribe(this::handleSuccessJobStart, this::handleFailureJobStart);
        compositeDisposable.addAll(d);
    }

    private void handleFailureJobStart(Throwable throwable) {
        LogUtils.logJob("error", throwable);
    }

    private void handleSuccessJobStart() {
        getViewState().jobStateChanged();
    }
}
