package org.doug.monitor.util;

import android.annotation.SuppressLint;
import android.net.ParseException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


@SuppressLint("SimpleDateFormat")
public class DateUtil {

    public static void main(String[] args) {
        String currentDate = getCurrentDate();
        System.out.println(currentDate);
    }


    private static SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");


    public static String getCurrentDate() {
        String currentDate = "";
        try {
            Calendar instance = Calendar.getInstance(Locale.CHINA);
            Date time = instance.getTime();
            String datetime = DEFAULT_FORMAT.format(time);
            currentDate = DATE_FORMAT.format(time);
            String format1 = TIME_FORMAT.format(time);
            System.out.println(currentDate);
            System.out.println(currentDate);
            System.out.println(format1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentDate;
    }

    public static String getCurrentTime() {
        String currentDate = "";
        try {
            Calendar instance = Calendar.getInstance(Locale.CHINA);
            Date time = instance.getTime();
            String datetime = DEFAULT_FORMAT.format(time);
            String date = DATE_FORMAT.format(time);
            currentDate = TIME_FORMAT.format(time);
            System.out.println(datetime);
            System.out.println(date);
            System.out.println(currentDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentDate;
    }


    /**
     * @return :String
     * @MethodName: getCurentTime
     * @Functions:Get the current time
     */
    public static String getCurentTime() {
        String date = defaultFormat.format(new Date());
        return date;
    }

    /**
     * @return :long
     * @throws ParseException
     * @MethodName: getCurentSecond
     * @Functions:The current time is converted into long type
     */
    public static long getCurentSecond() throws ParseException {
        String curentTime = getCurentTime();
        Date date = null;
        long beginTime;
        try {
            date = defaultFormat.parse(curentTime);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        beginTime = date.getTime();
        return beginTime / 1000;
    }

    public static long getTimeInterval(long startTime, long overTime) {
        return overTime - startTime;
    }

}
