package com.example.alexander.weatherapp.presenter;

import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.domain.interactor.FavoritesInteractor;
import com.yamblz.voltek.weather.presentation.ui.main.WeatherPresenter;
import com.yamblz.voltek.weather.presentation.ui.main.WeatherView$$State;
import com.yamblz.voltek.weather.presentation.ui.menu.items.WeatherItem;
import com.yamblz.voltek.weather.utils.SimpleMap;
import com.yamblz.voltek.weather.utils.classes.SetWithSelection;
import com.yamblz.voltek.weather.utils.rx.RxSchedulers;
import com.yamblz.voltek.weather.utils.rx.RxSchedulersTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.TreeSet;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created on 10.08.2017.
 */

public class WeatherPresenterTest {


    @Mock
    FavoritesInteractor interactor;

    @Mock
    WeatherView$$State presenterViewState;

    private WeatherPresenter presenter;

    private RxSchedulers rxSchedulers = new RxSchedulersTest();

    SetWithSelection<CityUIModel> setSpy;

    CityUIModel defaultSelected;

    @Before
    public void beforeEachTest() {

        MockitoAnnotations.initMocks(this);
        defaultSelected = new CityUIModel(777, "selected");
        when(interactor.favoritesDataAdded()).thenReturn(Observable.empty());
        SetWithSelection<CityUIModel> set = initSetWithSelection(defaultSelected);
        setSpy = spy(set);
        presenter = new WeatherPresenter(interactor, rxSchedulers, setSpy);
        presenter.setViewState(presenterViewState);


    }

    @Test
    public void firstAttachInitsDefaultsAndLoadsFavorites() {

        CityUIModel selected = new CityUIModel(1, "1");
        WeatherItem weatherItem = new WeatherItem(selected);
        SetWithSelection<CityUIModel> favorites = new SetWithSelection<>(new TreeSet<>((first, second) -> first.name.compareTo(second.name)), selected);

        when(interactor.initDefaultsSettings()).thenReturn(Completable.complete());
        when(interactor.getFavorites()).thenReturn(Single.just(favorites));
        when(interactor.selectCity(any(CityUIModel.class))).thenReturn(Completable.complete());
        doNothing().when(presenterViewState).navigateTo(any(), anyBoolean(), anyString(), any(SimpleMap.class));


        presenter.onFirstViewAttach();

        verify(presenterViewState, times(1)).setFavoritesItems(favorites);
        verify(presenterViewState, times(1)).navigateTo(any(), anyBoolean(), anyString(), any(SimpleMap.class));
        verify(presenterViewState, atLeast(1)).selectWeatherItem(weatherItem);
        verifyNoMoreInteractions(presenterViewState);
    }

    @Test
    public void weatherLongClickShowsDialog() {
        CityUIModel cityUIModel = new CityUIModel(1, "1");
        IDrawerItem clicked = new WeatherItem(cityUIModel);

        presenter.weatherClick(clicked, true);

        verify(presenterViewState, times(1)).showDialogForCity(cityUIModel);
        verifyNoMoreInteractions(presenterViewState);
    }

    @Test
    public void weatherSimpleClick() {

        CityUIModel clickedModel = new CityUIModel(1, "1");
        IDrawerItem clicked = new WeatherItem(clickedModel);

        when(interactor.selectCity(any())).thenReturn(Completable.complete());
        doNothing().when(presenterViewState).navigateTo(any(), anyBoolean(), anyString(), any(SimpleMap.class));

        presenter.weatherClick(clicked, false);

        verify(interactor, times(1)).selectCity(clickedModel);
        verify(presenterViewState, times(1)).selectWeatherItem((WeatherItem) clicked);
        verify(presenterViewState, times(1)).navigateTo(any(), anyBoolean(), anyString(), any(SimpleMap.class));
        verifyNoMoreInteractions(presenterViewState);
    }

    @Test
    public void deleteCityFromFavoriteSuccess() {
        CityUIModel clickedModel = new CityUIModel(1, "1");

        when(interactor.deleteFromFavorites(clickedModel, defaultSelected)).thenReturn(Single.just(defaultSelected));

        presenter.deleteCityFromFavorite(clickedModel);

        verify(setSpy, times(0)).addAsSelected(any(CityUIModel.class));
        verify(presenterViewState, times(1)).deleteWeatherItem(new WeatherItem(clickedModel));
        verifyNoMoreInteractions(presenterViewState);
    }

    @Test
    public void deleteCityFromFavoriteSuccessWhenIsSelected() {
        CityUIModel newSelectedItem = new CityUIModel(1, "1");

        when(interactor.deleteFromFavorites(defaultSelected, defaultSelected)).thenReturn(Single.just(newSelectedItem));
        when(interactor.selectCity(newSelectedItem)).thenReturn(Completable.complete());
        doNothing().when(presenterViewState).navigateTo(any(), anyBoolean(), anyString(), any(SimpleMap.class));

        presenter.deleteCityFromFavorite(defaultSelected);

        verify(setSpy, times(1)).deleteAndSetSelected(defaultSelected, newSelectedItem);
        verify(presenterViewState, times(1)).deleteWeatherItem(new WeatherItem(defaultSelected));
        verify(presenterViewState, times(1)).selectWeatherItem(new WeatherItem(newSelectedItem));
        verify(presenterViewState, times(1)).navigateTo(any(), anyBoolean(), anyString(), any(SimpleMap.class));
        verifyNoMoreInteractions(presenterViewState);
    }

    @Test
    public void deleteCityFromFavoriteFailure() {

        when(interactor.deleteFromFavorites(defaultSelected, defaultSelected)).thenReturn(Single.error(new RuntimeException()));

        presenter.deleteCityFromFavorite(defaultSelected);

        verify(presenterViewState, times(1)).showError(any());
        verifyNoMoreInteractions(presenterViewState);
    }


    private SetWithSelection<CityUIModel> initSetWithSelection(CityUIModel selected) {
        TreeSet<CityUIModel> treeSet = new TreeSet<>((first, second) -> first.name.compareTo(second.name));

        for (int i = 1000; i < 1010; i++) {
            treeSet.add(new CityUIModel(i, "item" + i));
        }

        return new SetWithSelection<>(treeSet, selected);
    }


}
