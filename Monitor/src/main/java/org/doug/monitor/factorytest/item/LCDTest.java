package org.doug.monitor.factorytest.item;

import android.graphics.Color;
import android.os.Handler;
import android.view.View;

import com.orhanobut.logger.Logger;

import org.doug.monitor.R;
import org.doug.monitor.factorytest.FactoryAutoTest;
import org.doug.monitor.base.util.DateUtil;


/**
 * LCDTest :The Test for LCD.
 */
public class LCDTest {

    private static String TAG = LCDTest.class.getSimpleName();
    public static boolean mIsLcdTestStop = false;

    public static long mLCDStartTime = 0;
    public static long mLCDOverTime = 0;
    public static long mLCDTestTime = 0;

    private final static Handler handler = new Handler();
    static int lcdtestkey = 1;
    private final static Runnable lcdtask = new Runnable() {
        public void run() {
            if (!mIsLcdTestStop) {
                switch (lcdtestkey) {
                    case 1:
                        mLCDStartTime = DateUtil.getCurentSecond();
                        FactoryAutoTest.mAllTestStartTime = DateUtil.getCurentSecond();
                        FactoryAutoTest.mLcdLinearLayout.setVisibility(View.VISIBLE);
                        Logger.d("LCDTest test testing");
                        lcdtestkey = 2;
                        handler.postDelayed(this, 900);
                        break;
                    case 2:
                        FactoryAutoTest.mLcdLinearLayout.setVisibility(View.INVISIBLE);
                        FactoryAutoTest.mainLayout.setBackgroundColor(Color.RED);
                        lcdtestkey = 3;
                        handler.postDelayed(this, 800);
                        break;
                    case 3:
                        FactoryAutoTest.mainLayout.setBackgroundColor(Color.GREEN);
                        lcdtestkey = 4;
                        handler.postDelayed(this, 800);
                        break;
                    case 4:
                        FactoryAutoTest.mainLayout.setBackgroundColor(Color.BLUE);
                        lcdtestkey = 5;
                        handler.postDelayed(this, 800);
                        break;
                    case 5:
                        FactoryAutoTest.mainLayout.setBackgroundColor(Color.BLACK);
                        lcdtestkey = 6;
                        handler.postDelayed(this, 800);
                        break;
                    case 6:
                        mIsLcdTestStop = true;
                        FactoryAutoTest.mainLayout.setBackgroundColor(Color.WHITE);
                        FactoryAutoTest.mTest1.setText(R.string.lcd);
                        mLCDOverTime = DateUtil.getCurentSecond();
                        mLCDTestTime = mLCDOverTime - mLCDStartTime;
                        Logger.d("LCDTest test over");
                        Logger.d("LCDTest test time: " + mLCDTestTime);
                        FactoryAutoTest.factoryAutoTest.handler.sendEmptyMessage(FactoryAutoTest.MSG_FORNT_CAMERA_TEST);
                        break;
                }
            }
        }
    };

    /**
     * Start LCD test.
     */
    public static void LcdTestCase() {
        lcdtestkey = 1;
        mIsLcdTestStop = false;
        FactoryAutoTest.TextViewColorChange(FactoryAutoTest.mTest8, FactoryAutoTest.mTest1);
        FactoryAutoTest.mTest1.setText(R.string.lcding);
        Logger.d("LCDTest test start");
        handler.post(lcdtask);
    }
}

	
	
	
