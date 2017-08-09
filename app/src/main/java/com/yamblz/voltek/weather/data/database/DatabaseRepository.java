package com.yamblz.voltek.weather.data.database;

import com.yamblz.voltek.weather.data.api.weather.models.forecast.ForecastResponseModel;
import com.yamblz.voltek.weather.data.database.models.CityToIDModel;
import com.yamblz.voltek.weather.data.database.models.FavoriteCityModel;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

/**
 * Created on 31.07.2017.
 */

public interface DatabaseRepository {

    Single<List<CityToIDModel>> getCityByPrefix(String prefix);

    Single<CityToIDModel> getCityByName(String name);

    Completable saveCity(CityToIDModel city);

    Completable saveAsFavoriteIfNotExists(CityToIDModel city, boolean notify);

    Single<List<FavoriteCityModel>> getFavorite();

    Completable deleteFromFavorites(CityUIModel cityUIModel);

    Single<FavoriteCityModel> getTopFavorite(int exclude);

    PublishSubject<FavoriteCityModel> getFavoritesAddedSubject();

    Completable updateFavorite(ForecastResponseModel forecastResponseModel);

    Single<ForecastResponseModel> getFavoriteForForecast(int id);
}
