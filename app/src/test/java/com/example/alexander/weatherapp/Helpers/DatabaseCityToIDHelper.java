package com.example.alexander.weatherapp.Helpers;


import com.yamblz.voltek.weather.data.database.models.CityToIDModel;

public class DatabaseCityToIDHelper {

    public static CityToIDModel getGoodModel() {
        return new CityToIDModel("Goodland", 777);
    }

    public static CityToIDModel getInvalidModel() {
        return new CityToIDModel("Badland", -1);
    }

}
