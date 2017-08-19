package com.example.alexander.weatherapp;


import com.yamblz.voltek.weather.R;
import com.yamblz.voltek.weather.domain.exception.RequestFailedException;
import com.yamblz.voltek.weather.utils.ResourceUtils;

import org.junit.Test;

import java.net.UnknownHostException;
import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class StringUtilsTest {


    @Test
    public void unknownThrowablePresentation() {
        Throwable throwable = new NullPointerException();
        assertEquals(ResourceUtils.fromError(throwable), R.string.er_unknown);
    }

    @Test
    public void apiThrowablePresentation() {
        Throwable[] throwable = {new RequestFailedException(), new UnknownHostException()};
        int[] expected = {R.string.er_request_failed, R.string.er_no_connection};
        int[] actual = new int[throwable.length];
        for (int i = 0; i < throwable.length; i++) {
            actual[i] = ResourceUtils.fromError(throwable[i]);
        }

        Arrays.sort(expected);
        Arrays.sort(actual);

        assertTrue(Arrays.equals(expected, actual));
    }

}
