package com.yamblz.voltek.weather.utils;

import android.support.compat.BuildConfig;
import android.util.Log;

/**
 * Created on 31.07.2017.
 */

public class LogUtils {

    public static void log(Object o) {
        if (BuildConfig.DEBUG)
            Log.e("TAG", " -> " + o);
    }

    public static void log(Object o, Throwable throwable) {
        if (BuildConfig.DEBUG)
            Log.e("TAG", " -> " + o, throwable);
    }

    public static void logWarning(Object o, Throwable throwable) {
        if (BuildConfig.DEBUG)
            Log.e("WARNING", " -> " + o, throwable);
    }

    public static void logJob(Object o) {
        if (BuildConfig.DEBUG)
            Log.e("JOB", " -> " + o);
    }

    public static void logJob(Object o, Throwable throwable) {
        if (BuildConfig.DEBUG)
            Log.e("JOB", " -> " + o, throwable);
    }
}
