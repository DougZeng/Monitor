package org.doug.monitor.factorytest.item;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.widget.TextView;


import com.orhanobut.logger.Logger;

import org.doug.monitor.R;
import org.doug.monitor.factorytest.FactoryAutoTest;
import org.doug.monitor.base.util.DateUtil;

import java.io.File;
import java.io.IOException;


public class RecordPlayTest {
    private static String TAG = RecordPlayTest.class.getSimpleName();

    public static MediaRecorder recorder;
    public static MediaPlayer media;
    public static AudioManager audioManager;
    public static File file;

    public static boolean isrecorder = false;
    public static boolean isplaying = false;
    public static boolean mIsRecordTestStop = false;

    private static TextView mRecordTextView;

    public static long mRecordStartTime = 0;
    public static long mRecordOverTime = 0;
    public static long mRecordTestTime = 0;

    public static void RecordPlayTestCase() {
        FactoryAutoTest.TextViewColorChange(FactoryAutoTest.mTest4, FactoryAutoTest.mTest5);
        RecordTaskStart();
    }

    private final static Handler handler = new Handler();
    static int recordtestkey = 1;
    private final static Runnable recordtask = new Runnable() {
        public void run() {
            if (!mIsRecordTestStop) {
                switch (recordtestkey) {
                    case 1:
                        mRecordTextView.setText(R.string.recording);
                        startRecord(); //start record
                        mRecordStartTime = DateUtil.getCurentSecond();
                        Logger.d("RecordPlayTest  record testing");
                        recordtestkey = 2;
                        handler.postDelayed(this, 5000);
                        break;
                    case 2:
                        mRecordTextView.setText(R.string.recordplaying);
                        stopRecordAndPlay(); //stop record ,play record
                        Logger.d("RecordPlayTest play testing");
                        recordtestkey = 3;
                        handler.postDelayed(this, 5000);
                        break;
                    case 3:
                        mIsRecordTestStop = true;
                        mRecordTextView.setText(R.string.recordtest);
                        stopPlayAndExit();
                        mRecordOverTime = DateUtil.getCurentSecond();
                        mRecordTestTime = mRecordOverTime - mRecordStartTime;
                        Logger.d("RecordPlayTest test over");
                        Logger.d("RecordPlayTest test time:" + mRecordTestTime);
                        FactoryAutoTest.factoryAutoTest.handler.sendEmptyMessage(FactoryAutoTest.MSG_VIBRATOR_TEST);
                        break;
                    default:
                        break;
                }
            }
        }
    };


    public static void RecordTaskStart() {
        InitAudio();
        recordtestkey = 1;
        mIsRecordTestStop = false;
        Logger.d("RecordPlayTest test start ");
        handler.post(recordtask);
    }

    public static void InitAudio() {
        mRecordTextView = FactoryAutoTest.mTest5;
        // get AUDIO_SERVICE
        audioManager = (AudioManager) FactoryAutoTest.mContext.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_NORMAL);// The pattern playback mode change to speaker
    }

    /**
     * @return void
     * @MethodName: startRecord
     * @Description:begin record  by mic,
     */
    public static void startRecord() {
        try {
            if (recorder == null) {
                //Initialization Recording
                recorder = new MediaRecorder();
            }
            //Set MIC-based mic
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //Set the recorder output format 3GP
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            //Coding :AMR_NB
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            //Save Path
            file = new File(Environment.getExternalStorageDirectory() + "/luyin.mp3");
            if (file.exists()) {
                file.delete();
            }
            recorder.setOutputFile(Environment.getExternalStorageDirectory() + "/luyin.mp3");
            //Ready
            recorder.prepare();
            //Start
            recorder.start();
            isrecorder = true;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @return void
     * @MethodName: stopRecord
     * @Description:stop record and playing record
     */
    public static void stopRecordAndPlay() {
        if (isrecorder) {
            if (file != null) {
                recorder.stop();
                recorder.release();
            }
            synchronized (file) {
                if (media == null) {
                    media = new MediaPlayer();
                }
                try {
                    media.setDataSource(Environment.getExternalStorageDirectory() + "/luyin.mp3");
                    media.prepare();
                    isplaying = true;
                    media.start();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        isrecorder = false;
    }


    /**
     * @return void
     * @MethodName: stopPlay
     * @Description:stop playing records
     */
    public static void stopPlayAndExit() {
        if (isplaying) {
            media.stop();
            media.reset();
            recorder = null;
            if (file.exists()) {
                file.delete();
            }
        }
        audioManager.setMode(AudioManager.MODE_NORMAL);
        if (media != null) {
            media.stop();
        }
        isplaying = false;
    }

}
