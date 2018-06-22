package com.doug.camera.lib;

import android.os.Environment;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by wesine on 2018/6/7.
 */

public class Util {

    public static String getRootDir() {
        File directory = Environment.getExternalStorageDirectory();
        if (!directory.exists()) {
            directory.mkdir();
        }
        File file = new File(directory, Environment.DIRECTORY_DCIM);
        return file.getAbsolutePath();
    }

    public static String getFilename() {
        StringBuffer buffer = new StringBuffer();
        Calendar instance = Calendar.getInstance(Locale.CHINA);
        int i = instance.get(Calendar.YEAR);
        buffer.append(i);
        int i1 = instance.get(Calendar.MONTH) + 1;
        buffer.append("-" + i1);
        int i2 = instance.get(Calendar.DAY_OF_MONTH);
        buffer.append("-" + i2);
        int i3 = instance.get(Calendar.HOUR_OF_DAY);
        buffer.append("-" + i3);
        int i4 = instance.get(Calendar.MINUTE);
        buffer.append("-" + i4);
        int i5 = instance.get(Calendar.SECOND);
        buffer.append("-" + i5);
        int i6 = instance.get(Calendar.MILLISECOND);
        buffer.append("-" + i6);
        return buffer.toString();
    }
}
