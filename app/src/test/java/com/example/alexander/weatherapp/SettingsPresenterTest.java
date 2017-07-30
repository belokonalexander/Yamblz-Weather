package com.example.alexander.weatherapp;


import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.domain.interactor.CurrentSettingsInteractor;
import com.yamblz.voltek.weather.domain.interactor.SettingsCitySuggestionsInteractor;
import com.yamblz.voltek.weather.domain.interactor.SettingsSetCityInteractor;
import com.yamblz.voltek.weather.presentation.ui.settings.SettingsPresenter;
import com.yamblz.voltek.weather.presentation.ui.settings.SettingsView;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



public class SettingsPresenterTest {

    private SettingsCitySuggestionsInteractor settingsCitySuggestionsInteractor;
    private SettingsSetCityInteractor settingsSetCityInteractor;
    private CurrentSettingsInteractor currentSettingsInteractor;
    private SettingsView view;
    private SettingsPresenter spyPresenter;

    @Before
    public void beforeEachTest() {
        settingsCitySuggestionsInteractor = mock(SettingsCitySuggestionsInteractor.class);
        settingsSetCityInteractor = mock(SettingsSetCityInteractor.class);
        currentSettingsInteractor = mock(CurrentSettingsInteractor.class);
        view = mock(SettingsView.class);
        SettingsPresenter presenter = new SettingsPresenter(settingsCitySuggestionsInteractor, settingsSetCityInteractor, currentSettingsInteractor);
        spyPresenter = spy(presenter);
        when(spyPresenter.getViewState()).thenReturn(view);

    }

    @Test
    public void afterInitPreTask() {

        verify(currentSettingsInteractor, times(1)).execute(any(), any(), any(), any());

    }


    @Test
    public void selectCityDelegatesToIteractorCorrect() {
        spyPresenter.selectCity("Moscow");
        spyPresenter.selectCity(new CityUIModel(10, "Moscow"));
        verify(settingsSetCityInteractor, times(2)).execute(any(), any(), any(), any());
    }

    @Test
    public void selectCitySendToViewDirectly() {
        doNothing().when(view).setCity(anyString());
        spyPresenter.selectCity(new CityUIModel(10, "Moscow"));
        verify(view, times(1)).setCity(anyString());
    }

    @Test
    public void emptySuggestionsForEmptyString() {
        doNothing().when(view).showSuggestions(any());
        spyPresenter.findSuggestions("");
        verify(view, times(1)).showSuggestions(any());
    }

    @Test
    public void startSuggestionsTaskForString() {
        doNothing().when(view).showSuggestions(any());
        doNothing().when(settingsCitySuggestionsInteractor).execute(any(), any(), any(), any());
        spyPresenter.findSuggestions("Some");
        verify(settingsCitySuggestionsInteractor, times(1)).execute(any(), any(), any(), any());
    }

    /*
       не могу протестирвать приватные методы-коллбеки onNext(), onComplete(), onError()
       можно ли определить приватные методы как stub методы?;
     */


}
