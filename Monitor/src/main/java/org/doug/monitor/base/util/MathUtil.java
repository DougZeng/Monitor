package org.doug.monitor.base.util;

import java.text.DecimalFormat;

/**
 * Created by wesine on 2018/5/30.
 */

public class MathUtil {
    public static DecimalFormat mFormat = new DecimalFormat("##,###,##0");
    public static DecimalFormat mFormatPercent = new DecimalFormat("##0.0");
    public static DecimalFormat mFormatTime = new DecimalFormat("0.#");

    public static float convertToGb(long valInBytes) {
        return Float.valueOf(String.format("%.2f", (float) valInBytes / (1024 * 1024 * 1024)));
    }

}
