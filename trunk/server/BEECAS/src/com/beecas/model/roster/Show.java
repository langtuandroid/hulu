package com.beecas.model.roster;

import java.util.Dictionary;
import java.util.Hashtable;

public enum Show {
    Offline(0), Online(1), Away(2), Busy(3), Invisible(4);

    private int value;

    private static Dictionary<Integer, Show> subMap;
    static {
        subMap = new Hashtable<Integer, Show>();
        subMap.put(Integer.valueOf(Offline.value), Offline);
        subMap.put(Integer.valueOf(Online.value), Online);
        subMap.put(Integer.valueOf(Away.value), Away);
        subMap.put(Integer.valueOf(Busy.value), Busy);
        subMap.put(Integer.valueOf(Invisible.value), Invisible);
    }

    Show(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static Show fromInteger(int value) {
        return subMap.get(Integer.valueOf(value));
    }
}
