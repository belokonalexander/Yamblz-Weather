package com.example.alexander.weatherapp;

import com.yamblz.voltek.weather.R;
import com.yamblz.voltek.weather.utils.WeatherUtils;

import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static junit.framework.Assert.assertEquals;

/**
 * Created on 09.08.2017.
 */

public class WeatherUtilsTest {


    @Test
    public void unexpectedConditionValue() {
        int result = WeatherUtils.getImageByCondition(146925234);
        assertEquals(-1, result);
    }

    @Test
    public void rainConditionTest() throws InterruptedException {
        TestObserver<Integer> testObserver = TestObserver.create();
        Observable.range(500, 5).map(WeatherUtils::getImageByCondition).subscribe(testObserver);

        testObserver.assertNoErrors();
        testObserver.assertValueSet(new HashSet<>(Collections.singletonList(R.drawable.ic_rain)));
    }

    @Test
    public void fogConditionTest() throws InterruptedException {
        TestObserver<Integer> testObserver = TestObserver.create();
        Observable.range(701, 61).map(WeatherUtils::getImageByCondition).subscribe(testObserver);

        testObserver.assertNoErrors();
        testObserver.assertValueSet(new HashSet<>(Collections.singletonList(R.drawable.ic_fog)));
    }

    @Test
    public void stormConditionTest() throws InterruptedException {
        TestObserver<Integer> testObserver = TestObserver.create();
        Observable.range(200, 33).map(WeatherUtils::getImageByCondition).subscribe(testObserver);

        testObserver.assertNoErrors();
        testObserver.assertValueSet(new HashSet<>(Collections.singletonList(R.drawable.ic_storm)));
    }

    @Test
    public void lightRainConditionTest() throws InterruptedException {
        TestObserver<Integer> testObserver = TestObserver.create();
        Observable.range(300, 22).map(WeatherUtils::getImageByCondition).subscribe(testObserver);

        testObserver.assertNoErrors();
        testObserver.assertValueSet(new HashSet<>(Collections.singletonList(R.drawable.ic_light_rain)));
    }

    @Test
    public void snowConditionTest() throws InterruptedException {
        TestObserver<Integer> testObserver = TestObserver.create();
        Observable.range(600, 23).map(WeatherUtils::getImageByCondition).subscribe(testObserver);
        testObserver.assertNoErrors();
        testObserver.assertValueSet(new HashSet<>(Collections.singletonList(R.drawable.ic_snow)));
    }

    @Test
    public void clearConditionTest() throws InterruptedException {
        TestObserver<Integer> testObserver = TestObserver.create();
        Observable.range(800, 1).map(WeatherUtils::getImageByCondition).subscribe(testObserver);

        testObserver.assertNoErrors();
        testObserver.assertValueSet(new HashSet<>(Collections.singletonList(R.drawable.ic_clear)));
    }

    @Test
    public void lightCloudsConditionTest() throws InterruptedException {
        TestObserver<Integer> testObserver = TestObserver.create();
        Observable.range(801, 1).map(WeatherUtils::getImageByCondition).subscribe(testObserver);

        testObserver.assertNoErrors();
        testObserver.assertValueSet(new HashSet<>(Collections.singletonList(R.drawable.ic_light_clouds)));
    }

    @Test
    public void cloudsConditionTest() throws InterruptedException {
        TestObserver<Integer> testObserver = TestObserver.create();
        Observable.range(802, 3).map(WeatherUtils::getImageByCondition).subscribe(testObserver);

        testObserver.assertNoErrors();
        testObserver.assertValueSet(new HashSet<>(Collections.singletonList(R.drawable.ic_clouds)));
    }


}
