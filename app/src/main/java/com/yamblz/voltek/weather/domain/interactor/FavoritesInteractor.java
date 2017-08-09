package com.yamblz.voltek.weather.domain.interactor;

import com.yamblz.voltek.weather.data.database.DatabaseRepository;
import com.yamblz.voltek.weather.data.storage.StorageRepository;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.domain.mappers.RxMapper;
import com.yamblz.voltek.weather.utils.classes.SetWithSelection;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created on 03.08.2017.
 */

public class FavoritesInteractor {

    private final DatabaseRepository databaseRepository;
    private final StorageRepository storageRepository;
    private final RxMapper rxMapper;

    public FavoritesInteractor(DatabaseRepository databaseRepository, StorageRepository storageRepository, RxMapper rxMapper) {
        this.databaseRepository = databaseRepository;
        this.storageRepository = storageRepository;
        this.rxMapper = rxMapper;
    }

    public Observable<CityUIModel> favoritesDataAdded() {
        return databaseRepository.getFavoritesAddedSubject()
                .map(rxMapper.favoriteCityModelToCityUIModel());
    }


    public Single<SetWithSelection<CityUIModel>> getFavorites() {
        return databaseRepository.getFavorite().map(rxMapper.favoriteCityModelListToCityUiModelTreeSet())
                .zipWith(storageRepository.getSelectedCity(), (cityUIModels, cityUIModel) -> {
                    cityUIModels.add(cityUIModel);
                    return new SetWithSelection<>(cityUIModels, cityUIModel);
                });
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

        if (!isCurrentSeleted)
            return databaseRepository.deleteFromFavorites(cityUIModel).toMaybe();
        else {
            return databaseRepository.deleteFromFavorites(cityUIModel)
                    .andThen(databaseRepository.getTopFavorite()
                            .map(rxMapper.favoriteCityModelToCityUIModel()))
                    .flatMap(new Function<CityUIModel, SingleSource<CityUIModel>>() {
                        @Override
                        public SingleSource<CityUIModel> apply(@NonNull CityUIModel cityUIModel) throws Exception {
                            return storageRepository.putSelectedCity(cityUIModel).toSingleDefault(cityUIModel);
                        }
                    }).toMaybe();
        }
    }
}
