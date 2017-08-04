package com.yamblz.voltek.weather.domain.interactor;

import com.yamblz.voltek.weather.data.database.DatabaseRepository;
import com.yamblz.voltek.weather.data.database.models.FavoriteCityModel;
import com.yamblz.voltek.weather.data.storage.StorageRepository;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.utils.classes.SetWithSelection;
import com.yamblz.voltek.weather.utils.rx.ListMapper;

import java.util.List;
import java.util.Set;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created on 03.08.2017.
 */

public class FavoritesInteractor {

    private DatabaseRepository databaseRepository;
    private StorageRepository storageRepository;

    public FavoritesInteractor(DatabaseRepository databaseRepository, StorageRepository storageRepository) {
        this.databaseRepository = databaseRepository;
        this.storageRepository = storageRepository;
    }

    public Single<SetWithSelection<CityUIModel>> getFavorites() {
        return databaseRepository.getFavorite().map(mapDBCityModelToUIModel())
                .zipWith(storageRepository.getSelectedCity(), (cityUIModels, cityUIModel) -> {
                    cityUIModels.add(cityUIModel);
                    return new SetWithSelection<>(cityUIModels, cityUIModel);
                });
    }

    private Function<List<FavoriteCityModel>, Set<CityUIModel>> mapDBCityModelToUIModel() {
        return ListMapper.mapToSet(another -> new CityUIModel(another.getCityId(), another.getAlias()));
    }

    public Completable selectCity(CityUIModel city) {
        return storageRepository.putSelectedCity(city);
    }

    public Completable initDefaultsSettings() {
        return storageRepository.getApplicationStartFirst()
                .filter(aBoolean -> aBoolean)
                .flatMapCompletable(aBoolean -> storageRepository.fillByDefaultData());
    }

    public Maybe<CityUIModel> deleteFromFavorites(CityUIModel cityUIModel, boolean isCurrentSeleted) {

        if(!isCurrentSeleted)
            return databaseRepository.deleteFromFavorites(cityUIModel).toMaybe();
        else {
            return databaseRepository.deleteFromFavorites(cityUIModel)
                    .andThen(databaseRepository.getTopFavorite()
                            .map(favoriteCityModel -> new CityUIModel(favoriteCityModel.getCityId(), favoriteCityModel.getAlias())))
                    .flatMap(new Function<CityUIModel, SingleSource<CityUIModel>>() {
                        @Override
                        public SingleSource<CityUIModel> apply(@NonNull CityUIModel cityUIModel) throws Exception {
                            return storageRepository.putSelectedCity(cityUIModel).toSingleDefault(cityUIModel);
                        }
                    }).toMaybe();
        }

    }
}
