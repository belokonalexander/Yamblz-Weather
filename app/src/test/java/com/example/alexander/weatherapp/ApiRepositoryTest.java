package com.example.alexander.weatherapp;


import com.example.alexander.weatherapp.Helpers.ApiWeatherModelHelper;
import com.yamblz.voltek.weather.data.api.weather.models.WeatherResponseModel;
import com.yamblz.voltek.weather.domain.entity.WeatherUIModel;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class ApiRepositoryTest {


    @Test
    public void correctMapResponseModelToUIModel() {
        WeatherResponseModel weatherResponseModel = ApiWeatherModelHelper.getWeatherModel();
        WeatherUIModel weatherUIModel = new WeatherUIModel(weatherResponseModel);
        assertEquals(weatherUIModel.getCondition(), weatherResponseModel.weather.get(0).description);
        assertEquals(weatherUIModel.getConditionId(), weatherResponseModel.weather.get(0).id);
        assertEquals(weatherUIModel.getHumidity(), (int) weatherResponseModel.main.humidity);
        assertEquals(weatherUIModel.getTemperature(), (int) weatherResponseModel.main.temp);
    }

    /**
     * server answer with no weather field - IndexOutOfBoundsException
     */
    @Test
    public void responseWithEmptyWeather() {
        WeatherResponseModel weatherResponseModel = ApiWeatherModelHelper
                .getWeatherModel((ApiWeatherModelHelper.Modify) jsonObject -> jsonObject.getAsJsonArray("weather").remove(0));

        WeatherUIModel weatherUIModel = new WeatherUIModel(weatherResponseModel);
        assertTrue(true);
    }

}
