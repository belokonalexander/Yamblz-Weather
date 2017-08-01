package com.example.alexander.weatherapp;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApiUtilsTest {


    @Test
    public void connectionIsFailed() {
        Context context = mock(Context.class);
        ConnectivityManager cm = mock(ConnectivityManager.class);
        when(cm.getActiveNetworkInfo()).thenReturn(null);
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(cm);
        assertEquals(false, ApiUtils.isConnected(context));
    }

    @Test
    public void connectionIsDisconnected() {
        Context context = mock(Context.class);
        ConnectivityManager cm = mock(ConnectivityManager.class);
        NetworkInfo ni = mock(NetworkInfo.class);
        when(ni.isConnected()).thenReturn(false);
        when(cm.getActiveNetworkInfo()).thenReturn(ni);
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(cm);
        assertEquals(false, ApiUtils.isConnected(context));
    }

    @Test
    public void connectionIsCorrect() {
        Context context = mock(Context.class);
        ConnectivityManager cm = mock(ConnectivityManager.class);
        NetworkInfo ni = mock(NetworkInfo.class);
        when(ni.isConnected()).thenReturn(true);
        when(cm.getActiveNetworkInfo()).thenReturn(ni);
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(cm);
        assertEquals(true, ApiUtils.isConnected(context));
    }

}
