package com.beecas.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class Utility {
    public static DateTime convertStringUpdateTimeToDateTime(String dateTimeString) { //"2010-09-28T03:59:18+0000" "2010-08-30T10:03:17+0000" "2010-09-02T07:14:26+0000"
        try {
            //                        int tIndex = dateTimeString.indexOf("T");
            //                        int plusIndex = dateTimeString.indexOf("+");
            //                        String dateData = dateTimeString.substring(0, tIndex);
            //                        String[] dateSplit = dateData.split("-");
            //                        String timeData = dateTimeString.substring(tIndex + 1, plusIndex);
            //                        String[] timeSplit = timeData.split(":");
            //                        DateTimeZone zoneUTC = DateTimeZone.UTC;
            //                        return new DateTime(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]), Integer.parseInt(dateSplit[2]), Integer.parseInt(timeSplit[0]), Integer.parseInt(timeSplit[1]), Integer
            //                                .parseInt(timeSplit[2]), 0, zoneUTC);
            //            String dateTimeFormat = dateTimeString.substring(0, dateTimeString.indexOf("+"));
            return new DateTime(dateTimeString, DateTimeZone.UTC);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DateTime(0, 0, 0, 0, 0, 0, 0, DateTimeZone.UTC);
    }

    public static String convertDateTimeUpdateTimeToString(DateTime dateTime) {
        try {
            return dateTime.toString("yyyy-MM-dd'T'HH:mm:ss");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static DateTime convertBirthdayStringToDateTime(String birthString) {
        try {
            //            String birthStringArray[] = birthString.split("/");
            return new DateTime(birthString, DateTimeZone.UTC);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DateTime(0, 0, 0, 0, 0, 0, 0, DateTimeZone.UTC);
    }

    public static String convertDateTimeBirthdayToString(DateTime dateTime) {
        try {
            return dateTime.toString("yyyy-MM-dd");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String convertDateTimeBirthdayOnlyDateToString(DateTime dateTime) {
        try {
            return dateTime.toString("MM-dd");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
