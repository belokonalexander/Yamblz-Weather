package com.yamblz.voltek.weather.data.database;

import android.database.sqlite.SQLiteConstraintException;

import com.yamblz.voltek.weather.data.database.models.CityToIDModel;
import com.yamblz.voltek.weather.data.database.models.CityToIDModelDao;
import com.yamblz.voltek.weather.data.database.models.DaoSession;

import java.util.Collection;

import io.reactivex.Completable;
import io.reactivex.Single;


public class DatabaseRepositoryImpl implements DatabaseRepository {

    private static final int DEFAULT_SUGGESTIONS_OFFSET = 40;

    private CityToIDModelDao cityToIDModelDao;


    public DatabaseRepositoryImpl(DaoSession session) {
        this.cityToIDModelDao = session.getCityToIDModelDao();
    }

    @Override
    public Single<Collection<CityToIDModel>> getCityByPrefix(String prefix) {
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
            e.printStackTrace();
        }
        return Completable.complete();
    }
}
