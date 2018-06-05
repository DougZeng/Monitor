package org.doug.monitor.base.util;

import android.os.Looper;
import android.util.Printer;

/**
 * Created by wesine on 2018/6/1.
 */

public class BlockDetectByPrinter {
    private static final String START = ">>>>> Dispatching";
    private static final String END = "<<<<< Finished";

    public static void start() {
        Looper.getMainLooper().setMessageLogging(new Printer() {
            @Override
            public void println(String x) {
                if (x.startsWith(START)) {
                    LogMonitor.getsInstance().startMonitor();
                }
                if (x.startsWith(END)) {
                    LogMonitor.getsInstance().startMonitor();
                }
            }
        });
    }
}
