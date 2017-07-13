package com.yamblz.voltek.weather.data;

/**
 * Набор интерфейсов для доступа к данным
 */
public final class Provider {

    private Provider() {}

    /**
     * Содержит интерфейсы для доступа к данным через API
     */
    public static class API {

        private API() {}

        public interface Weather {

            //
        }

        public interface Forecast {

            //
        }
    }

    /**
     * Содержит интерфейсы для доступа к локальному хранилищу
     */
    public static class Storage {

        private Storage() {}

        public interface Weather {

            //
        }

        public interface Forecast {

            //
        }
    }
}
