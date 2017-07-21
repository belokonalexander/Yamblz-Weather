package com.yamblz.voltek.weather.data.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class ApiUtils {

    public static boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = cm.getActiveNetworkInfo();

        return ni != null && ni.isConnected();
    }
}
