package org.doug.monitor.factorytest.item;

import android.app.Activity;
import android.app.Service;
import android.os.Handler;
import android.os.Vibrator;

import com.orhanobut.logger.Logger;

import org.doug.monitor.R;
import org.doug.monitor.factorytest.FactoryAutoTest;
import org.doug.monitor.base.util.DateUtil;


public class VibratorTest {

    private static String TAG = VibratorTest.class.getSimpleName();
    public static boolean mIsVibratorTestStop = false;
    // Frequency of the vibration of the vibrator
    private static long[] mPattern = new long[]{500, 200, 500, 200};
    // Vibrator class instantiation
    public static VibratorHelper mTipHelper;

    public static long mVibratorStartTime = 0;
    public static long mVibratorOverTime = 0;
    public static long mVibratorTestTime = 0;

    public static void VibratorTestCase() {
        FactoryAutoTest.TextViewColorChange(FactoryAutoTest.mTest5, FactoryAutoTest.mTest6);
        FactoryAutoTest.mTest6.setText(R.string.vibratoring);
        StartVibrator();
    }

    private final static Handler handler = new Handler();
    static int vibratortestkey = 1;
    private final static Runnable vibrarortask = new Runnable() {
        public void run() {
            if (!mIsVibratorTestStop) {
                switch (vibratortestkey) {
                    case 1:
                        motorTest();
                        mVibratorStartTime = DateUtil.getCurentSecond();
                        Logger.d("vibrator test testing");
                        vibratortestkey = 2;
                        handler.postDelayed(this, 5000);
                        break;
                    case 2:
                        mIsVibratorTestStop = true;
                        mTipHelper.cancel();
                        mVibratorOverTime = DateUtil.getCurentSecond();
                        mVibratorTestTime = mVibratorOverTime - mVibratorStartTime;
                        FactoryAutoTest.mTest6.setText(R.string.vibrator);
                        Logger.d("vibrator test over");
                        Logger.d("vibrator test time:" + mVibratorTestTime);
                        FactoryAutoTest.factoryAutoTest.handler.sendEmptyMessage(FactoryAutoTest.MSG_RING_TEST);
                        break;
                }
            }
        }
    };


    public static void StartVibrator() {
        vibratortestkey = 1;
        mIsVibratorTestStop = false;
        Logger.d("vibrator test start");
        handler.post(vibrarortask);

    }

    private static void motorTest() {
        // Create an instance and open the vibrator, and closed with button
        // events to control the vibrator
        mTipHelper = new VibratorHelper(FactoryAutoTest.factoryAutoTest);
        mTipHelper.vibrate(mPattern, true);
    }

    /**
     * @ClassName: TipHelper
     * @Description: Vibrator class mainly realize the two vibration modes, one
     * is a vibration only once, the other is a continuous
     * vibration
     */
    public static class VibratorHelper {
        Activity activity;
        Vibrator vib;

        public VibratorHelper(FactoryAutoTest factoryAutoTest) {
            this.activity = factoryAutoTest;
            vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        }

        public void vibrate(long milliseconds) {
            vib.vibrate(milliseconds);
        }

        public void vibrate(long[] pattern, boolean isRepeat) {
            vib.vibrate(pattern, isRepeat ? 1 : -1);
        }

        public void cancel() {
            vib.cancel();
        }
    }

}
