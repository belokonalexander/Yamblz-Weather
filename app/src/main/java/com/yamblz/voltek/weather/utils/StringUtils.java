package com.yamblz.voltek.weather.utils;

import com.yamblz.voltek.weather.R;
import com.yamblz.voltek.weather.domain.exception.DeleteLastCityException;
import com.yamblz.voltek.weather.domain.exception.RequestFailedException;

import java.net.UnknownHostException;

public final class StringUtils {

    private StringUtils() {
    }

    public static int fromError(Throwable e) {
        if (e instanceof UnknownHostException) return R.string.er_no_connection;
        else if (e instanceof RequestFailedException) return R.string.er_request_failed;
        else if (e instanceof DeleteLastCityException) return R.string.cant_delete_last_city;
        else return R.string.er_unknown;
    }
}
