package com.yamblz.voltek.weather.data.database;

import android.database.sqlite.SQLiteConstraintException;

import com.yamblz.voltek.weather.data.database.models.CityToIDModel;
import com.yamblz.voltek.weather.data.database.models.CityToIDModelDao;
import com.yamblz.voltek.weather.data.database.models.DaoSession;
import com.yamblz.voltek.weather.data.database.models.FavoriteCityModel;
import com.yamblz.voltek.weather.data.database.models.FavoriteCityModelDao;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.utils.LogUtils;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;


public class DatabaseRepositoryImpl implements DatabaseRepository {

    private static final int DEFAULT_SUGGESTIONS_OFFSET = 40;

    private CityToIDModelDao cityToIDModelDao;
    private FavoriteCityModelDao favoriteCitiesModelDao;
    private PublishSubject<FavoriteCityModel> favoriteCityModelPublishSubject = PublishSubject.create();

    public DatabaseRepositoryImpl(DaoSession session) {
        this.cityToIDModelDao = session.getCityToIDModelDao();
        this.favoriteCitiesModelDao = session.getFavoriteCityModelDao();
    }

    @Override
    public Single<List<CityToIDModel>> getCityByPrefix(String prefix) {
        return Single.fromCallable(() -> cityToIDModelDao.queryBuilder()
                .where(CityToIDModelDao.Properties.Alias.like(prefix + "%"))
                .limit(DEFAULT_SUGGESTIONS_OFFSET).list());
    }

    @Override
    public Single<CityToIDModel> getCityByName(String name) {
        return Single.fromCallable(() -> cityToIDModelDao.queryBuilder().where(CityToIDModelDao.Properties.Alias.like(name)).uniqueOrThrow());
    }

    @Override
    public Completable saveCity(CityToIDModel city) {
        try {
            cityToIDModelDao.save(city);
        } catch (SQLiteConstraintException e) {
            LogUtils.log(e.toString(), e);
        }
        return Completable.complete();
    }

    @Override
    public Completable saveAsFavorite(CityToIDModel city) {
        return Completable.fromAction(() -> {
            FavoriteCityModel favoriteCityModel = new FavoriteCityModel(city.getAlias(), city.getCityId());
            favoriteCitiesModelDao.save(favoriteCityModel);
            favoriteCityModelPublishSubject.onNext(favoriteCityModel);
        });
    }

    @Override
    public Single<List<FavoriteCityModel>> getFavorite() {
        return Single.fromCallable(() -> favoriteCitiesModelDao.loadAll());
    }

    @Override
    public Completable deleteFromFavorites(CityUIModel cityUIModel) {
        return Completable.fromAction(() -> favoriteCitiesModelDao.queryBuilder()
                .where(FavoriteCityModelDao.Properties.CityId.eq(cityUIModel.id)).buildDelete().executeDeleteWithoutDetachingEntities());
    }

    @Override
    public Single<FavoriteCityModel> getTopFavorite() {
        return Single.fromCallable(() -> favoriteCitiesModelDao.queryBuilder().limit(1).uniqueOrThrow());
    }

    @Override
    public PublishSubject<FavoriteCityModel> getFavoritesAddedSubject() {
        return favoriteCityModelPublishSubject;
    }


}
