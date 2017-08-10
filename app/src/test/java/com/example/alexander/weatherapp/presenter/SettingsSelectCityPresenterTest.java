package com.example.alexander.weatherapp.presenter;

import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.domain.interactor.CitySettingsInteractor;
import com.yamblz.voltek.weather.presentation.ui.adapter.models.CityAdapterItem;
import com.yamblz.voltek.weather.presentation.ui.settings.SettingsCityView$$State;
import com.yamblz.voltek.weather.presentation.ui.settings.SettingsSelectCityPresenter;
import com.yamblz.voltek.weather.utils.rx.RxSchedulers;
import com.yamblz.voltek.weather.utils.rx.RxSchedulersTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created on 10.08.2017.
 */

public class SettingsSelectCityPresenterTest {


    @Mock
    CitySettingsInteractor interactor;

    @Mock
    SettingsCityView$$State presenterViewState;

    private SettingsSelectCityPresenter presenter;

    private RxSchedulers rxSchedulers = new RxSchedulersTest();


    @Before
    public void beforeEachTest() {

        MockitoAnnotations.initMocks(this);
        presenter = new SettingsSelectCityPresenter(interactor, rxSchedulers);
        presenter.setViewState(presenterViewState);

    }

    @Test
    public void suggestionsWithNotEmptyString() {
        List<CityAdapterItem> expectedArray = new ArrayList<>();
        expectedArray.add(new CityAdapterItem(new CityUIModel(1,"1")));

        when(interactor.getCitySuggestions(anyString())).thenReturn(Single.just(expectedArray));

        presenter.findSuggestions("1");

        verify(presenterViewState, times(1)).showSuggestions(expectedArray);
    }

    @Test
    public void suggestionsWithEmptyString() {
        List<CityAdapterItem> expectedArray = new ArrayList<>();
        expectedArray.add(new CityAdapterItem(new CityUIModel(1,"1")));

        when(interactor.getCitySuggestions(anyString())).thenReturn(Single.just(expectedArray));

        presenter.findSuggestions("  ");

        verify(presenterViewState, times(0)).showSuggestions(expectedArray);
    }

    @Test
    public void suggestionsWithEror() {
        List<CityAdapterItem> expectedArray = new ArrayList<>();
        expectedArray.add(new CityAdapterItem(new CityUIModel(1,"1")));

        when(interactor.getCitySuggestions(anyString())).thenReturn(Single.error(new RuntimeException()));

        presenter.findSuggestions("1");

        verify(presenterViewState, times(0)).showSuggestions(expectedArray);
        verify(presenterViewState, times(1)).showError(any());
    }

    @Test
    public void selectCityWithString() {
        String text = "city";
        CityUIModel expected = new CityUIModel(1, text);

        when(interactor.saveCity(any(CityUIModel.class))).thenReturn(Single.just(expected));

        presenter.selectCity(text);

        verify(presenterViewState, times(1)).selectCity(expected);
        verify(presenterViewState, times(0)).showError(any());
    }

    @Test
    public void selectCityWithEmptyString() {
        String text = "  ";
        CityUIModel expected = new CityUIModel(1, text);

        when(interactor.saveCity(any(CityUIModel.class))).thenReturn(Single.just(expected));

        presenter.selectCity(text);

        verify(presenterViewState, times(0)).selectCity(expected);
        verify(presenterViewState, times(0)).showError(any());
    }

    @Test
    public void selectCityWithSaveError() {
        String text = "1";
        CityUIModel expected = new CityUIModel(1, text);

        when(interactor.saveCity(any(CityUIModel.class))).thenReturn(Single.error(new RuntimeException()));

        presenter.selectCity(text);

        verify(presenterViewState, times(0)).selectCity(expected);
        verify(presenterViewState, times(1)).showError(any());
    }

    @Test
    public void selectCityWithSaveErrorWhenIsEmpty() {
        String text = " ";
        CityUIModel expected = new CityUIModel(1, text);

        when(interactor.saveCity(any(CityUIModel.class))).thenReturn(Single.error(new RuntimeException()));

        presenter.selectCity(text);

        verify(presenterViewState, times(0)).selectCity(expected);
        verify(presenterViewState, times(0)).showError(any());
    }

    @Test
    public void selectCityFromSuggest() {
        String text = "item";
        CityUIModel saved = new CityUIModel(1, text);

        when(interactor.saveCity(saved)).thenReturn(Single.just(saved));
        presenter.selectCity(saved);

        verify(presenterViewState, times(1)).selectCity(saved);
        verify(presenterViewState, times(0)).showError(any());
    }

    @Test
    public void selectCityFromSuggestError() {
        String text = "item";
        CityUIModel saved = new CityUIModel(1, text);

        when(interactor.saveCity(saved)).thenReturn(Single.error(new RuntimeException()));
        presenter.selectCity(saved);

        verify(presenterViewState, times(0)).selectCity(saved);
        verify(presenterViewState, times(1)).showError(any());
    }

    @Test
    public void clearTextJustDoingIt() {

        presenter.clearTextView();

        verify(presenterViewState, times(1)).clearText();
        verifyNoMoreInteractions(presenterViewState);
    }

}
