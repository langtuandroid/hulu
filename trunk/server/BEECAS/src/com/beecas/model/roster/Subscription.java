package com.beecas.model.roster;

import java.util.Dictionary;
import java.util.Hashtable;

public enum Subscription {
    Both(8), From(2), Ignore(32), None(1), Remove(16), To(4);

    private int value;

    private static Dictionary<Integer, Subscription> subMap;
    static {
        subMap = new Hashtable<Integer, Subscription>();
        subMap.put(Integer.valueOf(Both.value), Both);
        subMap.put(Integer.valueOf(From.value), From);
        subMap.put(Integer.valueOf(Ignore.value), Ignore);
        subMap.put(Integer.valueOf(None.value), None);
        subMap.put(Integer.valueOf(Remove.value), Remove);
        subMap.put(Integer.valueOf(To.value), To);
    }

    Subscription(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static Subscription fromInteger(int value) {
        return subMap.get(Integer.valueOf(value));
    }
}
