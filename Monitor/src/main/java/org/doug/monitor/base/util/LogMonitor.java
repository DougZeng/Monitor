package org.doug.monitor.base.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.orhanobut.logger.Logger;

/**
 * Created by wesine on 2018/6/1.
 */

public class LogMonitor {
    private static LogMonitor sInstance = new LogMonitor();
    private HandlerThread mLogThread = new HandlerThread("log");
    private Handler mIoHandler;
    private static final long TIME_BLOCK = 16L;

    private LogMonitor() {
        mLogThread.start();
        mIoHandler = new Handler(mLogThread.getLooper());
    }

    private static Runnable mLogRunnable = new Runnable() {
        @Override
        public void run() {
            StringBuilder sb = new StringBuilder();
            StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
            for (StackTraceElement s : stackTrace) {
                sb.append(s.toString() + "\n");
            }
//            Logger.d("LogMonitor", sb.toString());
        }
    };

    public static LogMonitor getsInstance() {
        return sInstance;
    }

    //    public boolean isMonitor(){
//        return mIoHandler.hasCallbacks(mLogRunnable);
//    }
    public void startMonitor() {
        mIoHandler.postDelayed(mLogRunnable, TIME_BLOCK);
    }

    public void removeMonitor() {
        mIoHandler.removeCallbacks(mLogRunnable);
    }
}
