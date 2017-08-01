package com.yamblz.voltek.weather.utils;

import com.yamblz.voltek.weather.R;

public final class WeatherUtils {

    public static int getImageByCondition(int condition) {
        // Condition codes from here https://openweathermap.org/weather-conditions
        if (condition >= 200 && condition <= 232) return R.drawable.ic_storm;
        else if (condition >= 300 && condition <= 321) return R.drawable.ic_light_rain;
        else if (condition >= 500 && condition <= 504) return R.drawable.ic_rain;
        else if (condition == 511) return R.drawable.ic_snow;
        else if (condition >= 520 && condition <= 531) return R.drawable.ic_rain;
        else if (condition >= 600 && condition <= 622) return R.drawable.ic_snow;
        else if (condition >= 701 && condition <= 761) return R.drawable.ic_fog;
        else if (condition == 761 || condition == 781) return R.drawable.ic_storm;
        else if (condition == 800) return R.drawable.ic_clear;
        else if (condition == 801) return R.drawable.ic_light_clouds;
        else if (condition >= 802 && condition <= 804) return R.drawable.ic_clouds;
        return -1;
    }
}
