package com.yamblz.voltek.weather.data.database;

import com.yamblz.voltek.weather.data.database.models.CityToIDModel;
import com.yamblz.voltek.weather.data.database.models.FavoriteCityModel;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;

import java.util.Collection;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created on 31.07.2017.
 */

public interface DatabaseRepository {

    Single<Collection<CityToIDModel>> getCityByPrefix(String prefix);

    Single<CityToIDModel> getCityByName(String name);

    Completable saveCity(CityToIDModel city);

    Completable saveAsFavorite(CityToIDModel city);

    Single<List<FavoriteCityModel>> getFavorite();

    Completable deleteFromFavorites(CityUIModel cityUIModel);

    Single<FavoriteCityModel> getTopFavorite();
}
