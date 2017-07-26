package com.yamblz.voltek.weather.data.database;

import android.database.sqlite.SQLiteConstraintException;

import com.yamblz.voltek.weather.data.DataProvider;
import com.yamblz.voltek.weather.data.database.models.CityToIDModel;
import com.yamblz.voltek.weather.data.database.models.CityToIDModelDao;

import java.util.Collection;


public class CityRepositoryStorage implements DataProvider.DataBase.CityRepository {

    public static final int DEFAULT_OFFSET = 5;
    private CityToIDModelDao cityToIDModelDao;


    public CityRepositoryStorage(CityToIDModelDao cityToIDModelDao) {
        this.cityToIDModelDao = cityToIDModelDao;
    }

    @Override
    public Collection<CityToIDModel> getCityByPrefix(String prefix) {
        return cityToIDModelDao.queryBuilder().where(CityToIDModelDao.Properties.Alias.like("%" + prefix + "%")).limit(DEFAULT_OFFSET).list();
    }

    @Override
    public CityToIDModel getCityByName(String name) {
        return cityToIDModelDao.queryBuilder().where(CityToIDModelDao.Properties.Alias.like(name)).uniqueOrThrow();
    }

    @Override
    public CityToIDModel saveCity(CityToIDModel city) {
        try {
            cityToIDModelDao.save(city);
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }
        return city;
    }
}
