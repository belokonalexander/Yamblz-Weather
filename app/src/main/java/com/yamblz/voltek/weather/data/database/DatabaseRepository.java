package com.yamblz.voltek.weather.data.database;

import com.yamblz.voltek.weather.data.database.models.CityToIDModel;

import java.util.Collection;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created on 31.07.2017.
 */

public interface DatabaseRepository {

    Single<Collection<CityToIDModel>> getCityByPrefix(String prefix);

    Single<CityToIDModel> getCityByName(String name);

    Completable saveCity(CityToIDModel city);

}
