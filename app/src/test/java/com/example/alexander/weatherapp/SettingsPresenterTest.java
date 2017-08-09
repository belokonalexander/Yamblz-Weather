package com.example.alexander.weatherapp;


import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.presentation.ui.settings.SettingsCityView;
import com.yamblz.voltek.weather.presentation.ui.settings.SettingsSelectCityPresenter;

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
    private SettingsCityView view;
    private SettingsSelectCityPresenter spyPresenter;

    @Before
    public void beforeEachTest() {
        settingsCitySuggestionsInteractor = mock(SettingsCitySuggestionsInteractor.class);
        settingsSetCityInteractor = mock(SettingsSetCityInteractor.class);
        currentSettingsInteractor = mock(CurrentSettingsInteractor.class);
        view = mock(SettingsCityView.class);
        SettingsSelectCityPresenter presenter = new SettingsSelectCityPresenter(settingsCitySuggestionsInteractor, settingsSetCityInteractor, currentSettingsInteractor);
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
       не могу протестирвать приватные методы-коллбеки onNext(), onEmptyComplete(), onError()
       можно ли определить приватные методы как stub методы?;

       + как решение: если бы интерактор возвращал только Observable, а не выполнял подписку с делегированием callback
         методов в презентер, то можно было бы замокать интерактор и спокойно тестировать презентер, подставляя разные
         observable
     */


}
