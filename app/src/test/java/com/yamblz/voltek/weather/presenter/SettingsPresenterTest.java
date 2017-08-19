package com.yamblz.voltek.weather.presenter;

import com.yamblz.voltek.weather.domain.interactor.SettingsInteractor;
import com.yamblz.voltek.weather.presentation.ui.settings.SettingsPresenter;
import com.yamblz.voltek.weather.presentation.ui.settings.SettingsView$$State;
import com.yamblz.voltek.weather.utils.rx.RxSchedulers;
import com.yamblz.voltek.weather.utils.rx.RxSchedulersTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Completable;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created on 10.08.2017.
 */

public class SettingsPresenterTest {


    @Mock
    SettingsInteractor interactor;

    @Mock
    SettingsView$$State presenterViewState;

    private SettingsPresenter presenter;

    private RxSchedulers rxSchedulers = new RxSchedulersTest();


    @Before
    public void beforeEachTest() {

        MockitoAnnotations.initMocks(this);
        presenter = new SettingsPresenter(interactor, rxSchedulers);
        presenter.setViewState(presenterViewState);

    }

    @Test
    public void jobStateChanged() {

        when(interactor.updateJob()).thenReturn(Completable.complete());

        presenter.updateWeatherJob();

        verify(presenterViewState, times(1)).jobStateChanged();

    }



}
