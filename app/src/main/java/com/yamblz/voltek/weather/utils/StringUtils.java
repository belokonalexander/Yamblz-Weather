package com.yamblz.voltek.weather.utils;

import com.yamblz.voltek.weather.R;
import com.yamblz.voltek.weather.domain.exception.NoConnectionException;
import com.yamblz.voltek.weather.domain.exception.RequestFailedException;

public final class StringUtils {

    private StringUtils() {}

    public static int fromError(Throwable e) {
        if (e instanceof NoConnectionException) return R.string.er_no_connection;
        else if (e instanceof RequestFailedException) return R.string.er_request_failed;
        else return R.string.er_unknown;
    }
}
