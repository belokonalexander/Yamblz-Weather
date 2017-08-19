package com.yamblz.voltek.weather;

import com.yamblz.voltek.weather.helper.ApiDataHelper;
import com.yamblz.voltek.weather.data.api.weather.models.forecast.ForecastResponseModel;
import com.yamblz.voltek.weather.data.database.converters.ForecastConverter;

import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;

import static junit.framework.Assert.assertEquals;

/**
 * Created on 09.08.2017.
 */

public class ForecastConverterTest {

    ForecastConverter forecastConverter;

    @Before
    public void beforeEachTest() {
        forecastConverter = new ForecastConverter();
    }

    @Test
    public void convertTwoSideCorrect() {

        ForecastResponseModel forecastResponseModel = ApiDataHelper.getModel(ForecastResponseModel.class);

        String stringView = forecastConverter.convertToDatabaseValue(forecastResponseModel);

        ForecastResponseModel forecastResponseModelAnother = forecastConverter.convertToEntityProperty(stringView);

        String stringViewAnother = forecastConverter.convertToDatabaseValue(forecastResponseModelAnother);

        assertEquals(stringView, stringViewAnother);

    }

}
