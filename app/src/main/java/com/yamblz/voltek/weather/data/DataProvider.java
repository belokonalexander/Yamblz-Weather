package com.yamblz.voltek.weather.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.data.database.models.CityToIDModel;
import com.yamblz.voltek.weather.domain.entity.WeatherUIModel;
import com.yamblz.voltek.weather.domain.exception.NoConnectionException;
import com.yamblz.voltek.weather.domain.exception.RequestFailedException;

import java.util.Collection;

public final class DataProvider {

    private DataProvider() {}

    public static class API {

        private API() {}

        public interface Weather {

            WeatherUIModel getCurrent(String cityName) throws NoConnectionException, RequestFailedException;

            WeatherUIModel getCurrentByID(int id) throws NoConnectionException, RequestFailedException;

            CityUIModel getCity(String name) throws NoConnectionException, RequestFailedException;


        }
    }

    public static class Storage {

        private Storage() {}

        public interface Weather {

            @Nullable
            WeatherUIModel getCurrent();

            void putCurrent(@NonNull WeatherUIModel weatherUIModel);

            @NonNull
            CityUIModel putSelectedCity(@NonNull CityUIModel city);

            @NonNull
            CityUIModel getSelectedCity();
        }
    }

    public static class Database {

        private Database() {}

        public interface CityRepository {

            Collection<CityToIDModel> getCityByPrefix(String prefix);
            CityToIDModel getCityByName(String name);
            CityToIDModel saveCity(CityToIDModel city);
        }

    }
}
