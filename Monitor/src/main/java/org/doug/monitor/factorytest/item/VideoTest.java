package org.doug.monitor.factorytest.item;

import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.orhanobut.logger.Logger;

import org.doug.monitor.R;
import org.doug.monitor.factorytest.FactoryAutoTest;
import org.doug.monitor.base.util.DateUtil;


public class VideoTest {

    private static String TAG = VideoTest.class.getSimpleName();
    public static boolean mIsVideoTestStop = false;

    public static VideoView mVideoView;

    public static long mVideoStartTime = 0;
    public static long mVideoOverTime = 0;
    public static long mVideoTestTime = 0;

    public static void PlayMoviesTestCase() {
        FactoryAutoTest.TextViewColorChange(FactoryAutoTest.mTest7, FactoryAutoTest.mTest8);
        FactoryAutoTest.mTest8.setText(R.string.moviesing);
        StartVideo();
    }

    private final static Handler handler = new Handler();
    static int videotestkey = 1;
    private final static Runnable videotask = new Runnable() {
        public void run() {
            if (!mIsVideoTestStop) {
                switch (videotestkey) {
                    case 1:
                        videoInit();
                        mVideoView.setVisibility(View.VISIBLE);
                        Logger.d("video testing");
                        videotestkey = 2;
                        handler.postDelayed(this, 9000);
                        mVideoStartTime = DateUtil.getCurentSecond();
                        break;
                    case 2:
                        mIsVideoTestStop = true;
                        mVideoView.setVisibility(View.INVISIBLE);
                        stopPlayVideo();
                        mVideoOverTime = DateUtil.getCurentSecond();
                        mVideoTestTime = mVideoOverTime - mVideoStartTime;
                        FactoryAutoTest.mTest8.setText(R.string.movies);
                        FactoryAutoTest.mALLTestOverTime = DateUtil.getCurentSecond();
                        //FactoryAutoTest.mALLTestTime = FactoryAutoTest.mALLTestOverTime - FactoryAutoTest.mAllTestStartTime;

                        Logger.d("video test over");
                        Logger.d("video test time" + mVideoTestTime);
                        //Logset.logd("FactoryAuto", "All test time"+FactoryAutoTest.mALLTestTime);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        FactoryAutoTest.factoryAutoTest.handler.sendEmptyMessage(FactoryAutoTest.MSG_UPDATE_STATUS);
                        if (FactoryAutoTest.mCurrentTestTimes < FactoryAutoTest.mALLTestTimes) {
                            FactoryAutoTest.factoryAutoTest.handler.sendEmptyMessage(FactoryAutoTest.MSG_LCD_TEST);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    };

    public static void StartVideo() {
        videotestkey = 1;
        mIsVideoTestStop = false;
        mVideoView = FactoryAutoTest.mVideoView;
        Logger.d("video test start");
        handler.post(videotask);
    }

    /**
     * @return :void
     * @MethodName: videoInit
     * @Functions:videoview initialization
     */
    public static void videoInit() {
        mVideoView.setVideoURI(Uri.parse("android.resource://org.doug.monitor/" + R.raw.testmovies));
        MediaController mc = new MediaController(FactoryAutoTest.mContext);
        mc.setVisibility(View.INVISIBLE);//Hide the progress bar
        mVideoView.setMediaController(mc);
        mVideoView.start();
    }

    public static void stopPlayVideo() {
        mVideoView.pause();
        mVideoView.stopPlayback();
    }

}
