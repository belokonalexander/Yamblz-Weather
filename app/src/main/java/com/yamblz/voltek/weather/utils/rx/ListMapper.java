package com.yamblz.voltek.weather.utils.rx;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public static <T, S> Function<List<T>, Set<S>> mapToSet(SingleItemMapper<T, S> singleItemMapper) {
        return list -> {
            Set<S> dest = new HashSet<S>();
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
