package com.yamblz.voltek.weather.data.database.converters;

import com.google.gson.GsonBuilder;
import com.yamblz.voltek.weather.data.api.weather.models.forecast.ForecastResponseModel;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created on 07.08.2017.
 */

public class ForecastConverter implements PropertyConverter<ForecastResponseModel, String> {


    /*can't inject gson builder - documntation says:
        Note: For optimal performance, greenDAO will use a single converter instance for all conversions.
        Make sure the converter does not have any other constructor besides the parameter-less default constructor.
     */
    @Override
    public ForecastResponseModel convertToEntityProperty(String databaseValue) {
        return new GsonBuilder().create().fromJson(databaseValue, ForecastResponseModel.class);
    }

    @Override
    public String convertToDatabaseValue(ForecastResponseModel entityProperty) {
        return new GsonBuilder().create().toJson(entityProperty);
    }
}
