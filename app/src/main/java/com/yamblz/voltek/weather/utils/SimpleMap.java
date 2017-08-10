package com.yamblz.voltek.weather.utils;

/**
 * Created on 10.08.2017.
 */

import android.os.Bundle;

import java.util.HashMap;
import java.util.Set;

/**
 * for bundle analog in presenter layer
 */
public class SimpleMap extends HashMap<String, Object> {

    public void putInt(String key, int value) {
        put(key, value);
    }

    public void putString(String key, String value) {
        put(key, value);
    }

    public Integer getInt(String key) {
        if (containsKey(key) && get(key) instanceof Integer) {
            return (Integer) get(key);
        } else {
            return null;
        }
    }

    public String getString(String key) {
        if (containsKey(key) && get(key) instanceof String) {
            return (String) get(key);
        } else {
            return null;
        }
    }

    public static Bundle toBundle(SimpleMap simpleMap) {
        Set<String> keys = simpleMap.keySet();
        Bundle bundle = new Bundle();
        for (String key : keys) {
            Object item = simpleMap.get(key);
            if (item instanceof Integer) bundle.putInt(key, (Integer) item);
            else if (item instanceof String) bundle.putString(key, (String) item);
        }

        return bundle;
    }
}

