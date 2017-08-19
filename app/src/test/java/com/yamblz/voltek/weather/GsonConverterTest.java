package com.yamblz.voltek.weather;

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

    private GsonConverter gsonConverter;

    @Before
    public void beforeEachTest() {
        gsonConverter = new GsonConverter(new GsonBuilder().create());
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

    @Test
    public void reusableJsonTwoSide() {
        int id = 15;
        String name = "Name";

        CityUIModel cityUIModel = new CityUIModel(id, name);

        String s = gsonConverter.toJsonString(cityUIModel);

        CityUIModel out = gsonConverter.toJsonObject(CityUIModel.class, s);

        CityUIModel cityUIModelTwo = new CityUIModel(id + 5, name + "5");
        String sTwo = gsonConverter.toJsonString(cityUIModelTwo);
        CityUIModel outTwo = gsonConverter.toJsonObject(CityUIModel.class, sTwo);

        assertEquals(outTwo,cityUIModelTwo);
        assertEquals(cityUIModel, out);

    }

    @Test
    public void timeOfConverter() {
        CityUIModel cityUIModel = new CityUIModel(15, "Name");

        long time = System.currentTimeMillis();
        String s = gsonConverter.toJsonString(cityUIModel);
        System.out.println("time: " + (System.currentTimeMillis() - time) + " ms \nstring: " + s);
    }

}
