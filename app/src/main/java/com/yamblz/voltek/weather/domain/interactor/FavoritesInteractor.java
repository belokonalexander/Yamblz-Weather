package com.yamblz.voltek.weather.domain.interactor;

import com.yamblz.voltek.weather.data.database.DatabaseRepository;
import com.yamblz.voltek.weather.data.database.models.FavoriteCityModel;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.utils.rx.ListMapper;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.functions.Function;

/**
 * Created on 03.08.2017.
 */

public class FavoritesInteractor {

    private DatabaseRepository databaseRepository;

    public FavoritesInteractor(DatabaseRepository databaseRepository) {
        this.databaseRepository = databaseRepository;
    }

    public Single<List<CityUIModel>> getFavorites() {
        return databaseRepository.getFavorite().map(mapDBCityModelToUIModel());
    }

    private Function<List<FavoriteCityModel>, List<CityUIModel>> mapDBCityModelToUIModel() {
        return ListMapper.mapList(another -> new CityUIModel(another.getCityId(), another.getAlias()));
    }
}
