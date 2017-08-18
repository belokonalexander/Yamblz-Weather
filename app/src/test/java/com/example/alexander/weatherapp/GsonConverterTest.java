package com.example.alexander.weatherapp;

import com.google.gson.GsonBuilder;
import com.yamblz.voltek.weather.data.storage.GsonConverter;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created on 09.08.2017.
 */

public class GsonConverterTest {

    GsonConverter gsonConverter;

    @Before
    public void beforeEachTest() {
        gsonConverter = new GsonConverter(new GsonBuilder());
    }

    @Test
    public void toJsonTwoSide() {
        int id = 15;
        String name = "Name";

        CityUIModel cityUIModel = new CityUIModel(id, name);

        String s = gsonConverter.toJsonString(cityUIModel);

        CityUIModel out = gsonConverter.toJsonObject(CityUIModel.class, s);

        assertEquals(cityUIModel, out);

    }

}
