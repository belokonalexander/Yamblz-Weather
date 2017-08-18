package com.yamblz.voltek.weather.presentation.ui.main;

import android.support.annotation.VisibleForTesting;

import com.arellomobile.mvp.InjectViewState;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.domain.exception.DeleteLastCityException;
import com.yamblz.voltek.weather.domain.interactor.FavoritesInteractor;
import com.yamblz.voltek.weather.presentation.base.BasePresenter;
import com.yamblz.voltek.weather.presentation.ui.about.AboutFragment;
import com.yamblz.voltek.weather.presentation.ui.forecast.ForecastFragment;
import com.yamblz.voltek.weather.presentation.ui.menu.items.WeatherItem;
import com.yamblz.voltek.weather.presentation.ui.settings.SelectCityFragment;
import com.yamblz.voltek.weather.presentation.ui.settings.SettingsFragment;
import com.yamblz.voltek.weather.utils.LogUtils;
import com.yamblz.voltek.weather.utils.SimpleMap;
import com.yamblz.voltek.weather.utils.classes.SetWithSelection;
import com.yamblz.voltek.weather.utils.rx.RxSchedulers;

import io.reactivex.disposables.Disposable;

/**
 * Created on 03.08.2017.
 */

@InjectViewState
public class WeatherPresenter extends BasePresenter<WeatherView> {

    private final FavoritesInteractor interactor;
    private final RxSchedulers rxSchedulers;
    private SetWithSelection<CityUIModel> cities;

    public WeatherPresenter(FavoritesInteractor interactor, RxSchedulers rxSchedulers, SetWithSelection<CityUIModel> cities) {
        this.interactor = interactor;
        this.rxSchedulers = rxSchedulers;
        this.cities = cities;

        Disposable favoritesWatcher = interactor.favoritesDataAdded()
                .compose(rxSchedulers.getIOToMainTransformer(false))
                .subscribe(this::onCityAdded);

        compositeDisposable.addAll(favoritesWatcher);

    }


    @Override
    public void onFirstViewAttach() {
        super.onFirstViewAttach();
        initDefaultsSettings();
        initFavorites();

    }

    private void initDefaultsSettings() {
        Disposable initTask = interactor.initDefaultsSettings()
                .compose(rxSchedulers.getIOToMainTransformerCompletable())
                .subscribe();
        compositeDisposable.addAll(initTask);
    }

    private void initFavorites() {

        Disposable getFavoritesTask = interactor.getFavorites()
                .compose(rxSchedulers.getIOToMainTransformerSingle())
                .subscribe(this::onSuccesLoadFavorites, this::onError);
        compositeDisposable.addAll(getFavoritesTask);

    }

    private void onSuccesLoadFavorites(SetWithSelection<CityUIModel> cityUIModels) {
        cities = cityUIModels;
        inflateSideItemsMenu();
    }

    private void inflateSideItemsMenu() {
        getViewState().setFavoritesItems(cities);
        WeatherItem selected = new WeatherItem(cities.getSelectedItem());
        getViewState().selectWeatherItem(selected);
        weatherClick(selected, false);
    }

    private void onError(Throwable throwable) {
        LogUtils.log("Error: " + throwable, throwable);
    }

    public void weatherClick(IDrawerItem drawerItem, boolean longClick) {

        WeatherItem weatherItem = (WeatherItem) drawerItem;
        CityUIModel selectedCity = weatherItem.getModel();

        if (!longClick) {
            Disposable setSelectedCityTask = interactor.selectCity(selectedCity)
                    .compose(rxSchedulers.getIOToMainTransformerCompletable())
                    .subscribe(() -> {
                        cities.addAsSelected(selectedCity);
                        getViewState().selectWeatherItem(weatherItem);
                        SimpleMap bundle = new SimpleMap();
                        bundle.putString(ForecastFragment.TITLE_ARG, selectedCity.name);
                        getViewState().navigateTo(ForecastFragment.class, true, ForecastFragment.class.getName() + selectedCity.id, bundle);
                    }, throwable -> LogUtils.log("error: ", throwable));

            compositeDisposable.addAll(setSelectedCityTask);
        } else {
            getViewState().showDialogForCity(selectedCity);
        }

    }

    public void navigateToSettings() {
        getViewState().navigateTo(SettingsFragment.class, false, SettingsFragment.class.getName(), null);
        getViewState().selectStickyItem();
    }

    public void navigateToAbout() {
        getViewState().navigateTo(AboutFragment.class, false, AboutFragment.class.getName(), null);
        getViewState().selectStickyItem();
    }

    @VisibleForTesting
    private void onCityAdded(CityUIModel cityUIModel) {
        cities.addAsSelected(cityUIModel);
        inflateSideItemsMenu();
        getViewState().scrollToElement(new WeatherItem(cityUIModel));
    }

    public void navigateToAddCity() {

        SimpleMap bundle = new SimpleMap();

        if (cities.getSelectedItem() != null) {
            bundle.putInt(SelectCityFragment.CURRENT_SELECTED_CITY, cities.getSelectedItem().id);
        }

        getViewState().navigateTo(SelectCityFragment.class, false, SelectCityFragment.class.getName(), bundle);
        getViewState().selectStickyItem();
    }


    public void deleteCityFromFavorite(CityUIModel selectedCity) {
        Disposable deleteTask = interactor.deleteFromFavorites(selectedCity, cities.getSelectedItem())
                .compose(rxSchedulers.getIOToMainTransformerSingle())
                .subscribe(cityUIModel -> {
                    if (cityUIModel.equals(cities.getSelectedItem())) {
                        cities.delete(selectedCity);
                        getViewState().deleteWeatherItem(new WeatherItem(selectedCity));
                    } else {
                        cities.deleteAndSetSelected(selectedCity, cityUIModel);
                        getViewState().deleteWeatherItem(new WeatherItem(selectedCity));

                        WeatherItem newSelectItem = new WeatherItem(cityUIModel);
                        weatherClick(newSelectItem, false);
                    }
                }, throwable -> getViewState().showError(new DeleteLastCityException()));


        compositeDisposable.addAll(deleteTask);

    }
}
