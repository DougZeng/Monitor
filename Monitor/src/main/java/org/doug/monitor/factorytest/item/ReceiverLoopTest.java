package org.doug.monitor.factorytest.item;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Handler;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.doug.monitor.R;
import org.doug.monitor.factorytest.FactoryAutoTest;
import org.doug.monitor.util.DateUtil;


/**
 * ReceiverTest :The Test for Receiver.
 */
public class ReceiverLoopTest {

    private static String TAG = ReceiverLoopTest.class.getSimpleName();

    public static AudioManager mAudioManager = null;

    private static TextView mReceiverTextView;

    public static long mReceiverStartTime = 0;
    public static long mReceiverOverTime = 0;
    public static long mReceiverTestTime = 0;

    public static boolean mIsReceiverTestStop = false;

    public static void ReceiverTestCase() {
        StartReceiver();
        if (CameraTest.FindBackCameraExist()) {
            FactoryAutoTest.TextViewColorChange(FactoryAutoTest.mTest3, FactoryAutoTest.mTest4);
        } else {
            FactoryAutoTest.mTest4.setTextColor(Color.BLUE);
        }
    }

    private final static Handler handler = new Handler();
    static int receivertestkey = 1;
    private final static Runnable receivertask = new Runnable() {
        public void run() {
            if (!mIsReceiverTestStop) {
                switch (receivertestkey) {
                    case 1:
                        mReceiverTextView.setText(R.string.receivering);
                        new MicloopThread().start(); // resume the activity ,start the MicloopThread
                        mReceiverStartTime = DateUtil.getCurentSecond();
                        Logger.d("ReceiverTest testing");
                        receivertestkey = 2;
                        handler.postDelayed(this, 5000);
                        break;
                    case 2:
                        mIsReceiverTestStop = true;
                        mAudioManager.setParameters("SET_LOOPBACK_TYPE=0");
                        mReceiverOverTime = DateUtil.getCurentSecond();
                        mReceiverTestTime = mReceiverOverTime - mReceiverStartTime;
                        Logger.d("ReceiverTest test over");
                        Logger.d("ReceiverTest test time:" + mReceiverTestTime);
                        mReceiverTextView.setText(R.string.str_receiver);

                        FactoryAutoTest.factoryAutoTest.handler.sendEmptyMessage(FactoryAutoTest.MSG_RECORD_TEST);
                        break;
                    default:
                        break;
                }
            }
        }
    };

    public static void StartReceiver() {
        receivertestkey = 1;
        mIsReceiverTestStop = false;
        mReceiverTextView = FactoryAutoTest.mTest4;
        mAudioManager = ((AudioManager) FactoryAutoTest.mContext.getSystemService(Context.AUDIO_SERVICE));
        Logger.d("ReceiverTest test start");
        handler.post(receivertask);

    }

    /**
     *
     */
    static class MicloopThread extends Thread {
        @Override
        public void run() {
            if (!mIsReceiverTestStop) {
                //Thread.sleep(100); //At least 1 s between state action
                mAudioManager.setParameters("SET_LOOPBACK_TYPE=21");
                return;
            }
        }
    }

}
