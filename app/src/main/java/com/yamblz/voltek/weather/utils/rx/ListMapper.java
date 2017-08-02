package com.yamblz.voltek.weather.utils.rx;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Function;

/**
 * Created on 02.08.2017.
 */

public class ListMapper {

    public static <T, S> Function<List<T>, List<S>> mapList(SingleItemMapper<T, S> singleItemMapper) {
        return list -> {

            List<S> dest = new ArrayList<>();

            for(T sourceItem : list) {
                dest.add(singleItemMapper.getItem(sourceItem));
            }

            return dest;
        };
    }

    public interface SingleItemMapper<T, S> {
        S getItem(T another);
    }

}
