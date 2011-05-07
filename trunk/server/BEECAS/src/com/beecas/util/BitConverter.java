package com.beecas.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;

public class BitConverter {
	public static byte[] getBytes(long number) {
		byte[] result = new byte[8];
		for (int i = 7; i >= 0; i--) {
			result[7 - i] = (byte) (number >> i * 8);
		}
		return result;
	}

	public static byte[] getBytes(int number) {
		byte[] result = new byte[4];
		for (int i = 3; i >= 0; i--) {
			result[3 - i] = (byte) (number >> i * 8);
		}
		return result;
	}

	private static byte[] EMPTY = new byte[0];

	public static byte[] getBytes(String s) {
		if (s == null) {
			return EMPTY;
		}
		try {
			return s.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static long getLong(byte[] value) {
		long res = 0;
		for (int i = 0; i < 8; i++) {
			res <<= 8;
			res |= (0xFF & value[i]);
		}
		return res;
	}
	
	public static int getInt(byte[] value) {
		int res = 0;
		for (int i = 0; i < 4; i++) {
			res <<= 8;
			res |= (0xFF & value[i]);
		}
		return res;
	}
	
	public static String getString(byte[] value) {
		if (value.length == 0) {
			return null;
		}
		try {
			return new String(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static byte[] toBytes(Object o){
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
    	ObjectOutputStream oo;
		try {
			oo = new ObjectOutputStream(bo);
			oo.writeObject(o);
			return bo.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return null;
	}
	
	public static Object fromBytes(byte[] bytes) {
		ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
    	try {
			ObjectInputStream oi = new ObjectInputStream(bi);
			return oi.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
