package org.doug.monitor.base.util;

import java.text.DecimalFormat;

/**
 * Created by wesine on 2018/5/30.
 */

public class MathUtil {

    public static void main(String[] args) {

    }

    public static DecimalFormat mFormat = new DecimalFormat("##,###,##0");
    public static DecimalFormat mFormatPercent = new DecimalFormat("##0.0");
    public static DecimalFormat mFormatTime = new DecimalFormat("0.#");

    public static float convertToGb(long valInBytes) {
        return Float.valueOf(String.format("%.2f", (float) valInBytes / (1024 * 1024 * 1024)));
    }


    public static String stringToAscii(String value) {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i != chars.length - 1) {
                sbu.append((int) chars[i]).append(",");
            } else {
                sbu.append((int) chars[i]);
            }
        }
        return sbu.toString();
    }

    public static String asciiToString(String value) {
        StringBuffer sbu = new StringBuffer();
        String[] chars = value.split(",");
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) Integer.parseInt(chars[i]));
        }
        return sbu.toString();
    }

}
