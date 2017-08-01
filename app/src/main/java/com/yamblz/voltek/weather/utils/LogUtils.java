package com.yamblz.voltek.weather.utils;

import android.util.Log;

/**
 * Created on 31.07.2017.
 */

public class LogUtils {

    public static void log(Object o) {
        Log.e("TAG", " -> " + o);
    }

    public static void log(Object o, Throwable throwable) {
        Log.e("TAG", " -> " + o, throwable);
    }

}
