package com.yamblz.voltek.weather.presentation.ui.main;

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
import com.yamblz.voltek.weather.utils.classes.SetWithSelection;
import com.yamblz.voltek.weather.utils.rx.RxSchedulers;

import io.reactivex.disposables.Disposable;

/**
 * Created on 03.08.2017.
 */

@InjectViewState
public class WeatherPresenter extends BasePresenter<WeatherView> {

    private FavoritesInteractor interactor;
    private RxSchedulers rxSchedulers;
    private SetWithSelection<CityUIModel> cities;

    public WeatherPresenter(FavoritesInteractor interactor, RxSchedulers rxSchedulers) {
        this.interactor = interactor;
        this.rxSchedulers = rxSchedulers;
        this.cities = new SetWithSelection<>();
    }


    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        initDefaultsSettings();
        initFavorites();

    }

    private void initDefaultsSettings() {
        Disposable initTAsk = interactor.initDefaultsSettings()
                .compose(rxSchedulers.getIOToMainTransformerCompletable())
                .subscribe();
        compositeDisposable.addAll(initTAsk);
    }

    private void initFavorites() {

        Disposable getFavoritesTask = interactor.getFavorites()
                .compose(rxSchedulers.getIOToMainTransformerSingle())
                .subscribe(this::onSuccesLoadFavorites, this::onError);
        compositeDisposable.addAll(getFavoritesTask);

    }

    private void onSuccesLoadFavorites(SetWithSelection<CityUIModel> cityUIModels) {
        cities = cityUIModels;
        getViewState().setFavoritesItems(cities);
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
                        getViewState().navigateTo(ForecastFragment.class, true, null);
                    }, throwable -> LogUtils.log("error: ", throwable));

            compositeDisposable.addAll(setSelectedCityTask);
        } else {
            getViewState().showDialogForCity(selectedCity);
        }

    }

    public void navigateToSettings() {
        getViewState().navigateTo(SettingsFragment.class, false, null);
    }

    public void navigateToAbout() {
        getViewState().navigateTo(AboutFragment.class, false, null);
    }

    private void onCityAdded(CityUIModel cityUIModel) {
        cities.addAsSelected(cityUIModel);
        getViewState().setFavoritesItems(cities);
    }

    public void navigateToAddCity() {
        getViewState().navigateTo(SelectCityFragment.class, false, result -> {
            if (result instanceof CityUIModel) {
                onCityAdded((CityUIModel) result);
            }
        });
    }


    void deleteCityFromFavorite(CityUIModel selectedCity) {
        Disposable deleteTask = interactor.deleteFromFavorites(selectedCity, cities.getSelectedItem().equals(selectedCity))
                .compose(rxSchedulers.getIOToMainTransformerMaybe())
                .subscribe(cityUIModel -> {
                    cities.deleteAndSetSelected(selectedCity, cityUIModel);
                    getViewState().setFavoritesItems(cities);
                }, throwable -> getViewState().showError(new DeleteLastCityException()), () -> {
                    cities.delete(selectedCity);
                    getViewState().setFavoritesItems(cities);
                });

        compositeDisposable.addAll(deleteTask);

    }
}
