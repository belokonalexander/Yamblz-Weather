package com.yamblz.voltek.weather.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yamblz.voltek.weather.domain.entity.WeatherUIModel;
import com.yamblz.voltek.weather.domain.exception.NoConnectionException;
import com.yamblz.voltek.weather.domain.exception.RequestFailedException;

public final class Provider {

    private Provider() {}

    public static class API {

        private API() {}

        public interface Weather {

            WeatherUIModel getCurrent() throws NoConnectionException, RequestFailedException;
        }
    }

    public static class Storage {

        private Storage() {}

        public interface Weather {

            @Nullable
            WeatherUIModel getCurrent();

            void putCurrent(@NonNull WeatherUIModel weatherUIModel);
        }
    }
}
