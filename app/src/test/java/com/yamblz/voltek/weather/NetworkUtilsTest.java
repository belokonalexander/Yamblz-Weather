package com.yamblz.voltek.weather;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.yamblz.voltek.weather.data.api.NetworkUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created on 09.08.2017.
 */

public class NetworkUtilsTest {

    public NetworkUtils networkUtils;
    public Context context;

    @Before
    public void beforeEachTest() {
        context = mock(Context.class);
        networkUtils = new NetworkUtils(context);
    }

    @Test
    public void connectionIsFailed() {
        ConnectivityManager cm = mock(ConnectivityManager.class);
        when(cm.getActiveNetworkInfo()).thenReturn(null);
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(cm);
        assertEquals(false, networkUtils.isNetworkAvailable());
    }


    @Test
    public void connectionIsCorrect() {
        ConnectivityManager cm = mock(ConnectivityManager.class);
        NetworkInfo ni = mock(NetworkInfo.class);
        when(cm.getActiveNetworkInfo()).thenReturn(ni);
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(cm);
        assertEquals(true, networkUtils.isNetworkAvailable());
    }


}
