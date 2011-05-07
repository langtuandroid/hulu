package com.beecas.model;

import java.util.Dictionary;
import java.util.Hashtable;

public enum LoginResult {
    Success(1), InvalidAuthentication(2), NotYetRegister(3), UnknowFormat(4);

    private int value;

    private static Dictionary<Integer, LoginResult> subMap;
    static {
        subMap = new Hashtable<Integer, LoginResult>();
        subMap.put(Integer.valueOf(Success.value), Success);
        subMap.put(Integer.valueOf(InvalidAuthentication.value), InvalidAuthentication);
        subMap.put(Integer.valueOf(NotYetRegister.value), NotYetRegister);
    }

    LoginResult(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static LoginResult fromInteger(int value) {
        return subMap.get(Integer.valueOf(value));
    }
}
