package org.doug.monitor.factorytest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.orhanobut.logger.Logger;

import org.doug.monitor.R;
import org.doug.monitor.base.App;
import org.doug.monitor.base.Constans;
import org.doug.monitor.base.util.AnrMonitor;
import org.doug.monitor.base.util.SharedPreferencesUtils;
import org.doug.monitor.base.util.Toaster;
import org.doug.monitor.factorytest.item.CameraTest;
import org.doug.monitor.factorytest.item.LCDTest;
import org.doug.monitor.factorytest.item.ReceiverLoopTest;
import org.doug.monitor.factorytest.item.ReceiverThreadTest;
import org.doug.monitor.factorytest.item.RecordPlayTest;
import org.doug.monitor.factorytest.item.RingerTest;
import org.doug.monitor.factorytest.item.VibratorTest;
import org.doug.monitor.factorytest.item.VideoTest;
import org.doug.monitor.base.util.KeyBoardUtils;
import org.doug.monitor.factorytest.view.ListViewForReport;


/**
 *
 */
public class FactoryAutoTest extends Activity implements Toaster.DialogCallback, MsgTest {

    private static String TAG = FactoryAutoTest.class.getSimpleName();

    // this class' object
    public static FactoryAutoTest factoryAutoTest;
    public static Context mContext;

    //Layout ID
    public static final int TEST_MAIN_LAYOUT = R.id.rel_mian;
    public static final int TEST_LCD_LAYOUT = R.id.showlcdbackground;
    public static final int TEST_SURFACEVIEW_LAYOUT = R.id.surface;
    public static final int TEST_VIDEOVIEW_LAYOUT = R.id.videoView;
    public static final int TEST_SETTING_LAYOUT = R.id.ll_controlbar;
    public static final int TEST_REPORT_LAYOUT = R.id.report_listview;
    public static final int TEST_LIST_LAYOUT = R.id.ll_test;
    public static final int TEST_TIMES_ET = R.id.edit_testtimes;
    public static final int TEST_START_BT = R.id.but_start;
    public static final int TEST_STOP_BT = R.id.but_stop;
    public static final int TEST_REPORT_BT = R.id.but_report;
    public static final int TEST_ALL_TIMES_TV = R.id.txt_alltimes;
    public static final int TEST_CUR_TIMES_TV = R.id.txt_curtimes;
    public static final int TEST_REPORT_ITEM = R.id.reportlist_item;
    public static final int TEST_RESULT_TV = R.id.tv_result;


    //Layout-related; UI
    public static RelativeLayout mainLayout;
    public static LinearLayout mLcdLinearLayout;
    public static LinearLayout mSettingLinearLayout;
    public static RelativeLayout mrlWhole;
    public static LinearLayout mAllTestLinearLayout;
    public static SurfaceView mSurfaceView;
    public static VideoView mVideoView;
    public static Button mStartButton, mStopButton, mReportButton;
    public static EditText mEditTestTimesEditText;
    public static TextView mAllTestTimesTextView, mCurrentTestTimesTextView, mAllTestResult;
    public static ListView mReportListView;
    public static RelativeLayout mReportListItem;
    public static TextView mTest1;
    public static TextView mTest2;
    public static TextView mTest3;
    public static TextView mTest4;
    public static TextView mTest5;
    public static TextView mTest6;
    public static TextView mTest7;
    public static TextView mTest8;

    //Testing times
    public static int mCurrentTestTimes = 0;
    public static int mALLTestTimes = 0;

    //Testing Time
    public static long mAllTestStartTime = 0;
    public static long mALLTestOverTime = 0;
    public static long mALLTestTime = 0;

    //Testing results
    public static String mTestResult = null;
    public static int mTestResultColor = 0;
    public static int mSetAllTestTime = 60;

    //Testing flags
    public static boolean TEST_RESULT_PASS_FLAG = true;
    private static boolean IS_REPORT_FLAG = false;
    private static boolean TEST_OVER_FLAG = false;
    private static boolean TEST_START_FLAG = false;
    private static boolean TEST_START_FLAG_CHARGEING = false; //chb add for VFOZBAUBQ-179 at 2015-11-9

    private static boolean TEST_AUTO_FLAG = false;

    private int resultCount;


    /**
     * @Fields: mBatteryReceiver
     * @Description:judgment of the phone battery.
     * <br> Only when more than 40% of electricity,can to run the program.
     */
    private final BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                //int level = (int) (100f * intent.getIntExtra("level", 0) / intent.getIntExtra("scale", 100));
                int current = intent.getExtras().getInt("level");//Get the current power
                int total = intent.getExtras().getInt("scale");//Get the total power
                int percent = current * 100 / total;
                Logger.d("percent :" + percent);
                if (percent >= 10) {//chb modify for VFOZBAUBQ-181 at 20151111 old :40%
                    TEST_START_FLAG = true;
                } else {
                    TEST_START_FLAG = false;
                }
            }
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;
            if (isCharging) {
                TEST_START_FLAG_CHARGEING = true;
            } else {
                TEST_START_FLAG_CHARGEING = false;
            }
        }
    };

    /**
     * @Fields: handler
     * @Founction: Receiving a message, perform a process corresponding.
     */
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Logger.d("get message ,start action");
            if (!TEST_OVER_FLAG) {
                switch (msg.what) {
                    /** 1.MSG_LCD_TEST: start lcd testcase */
                    case MSG_LCD_TEST:
                        VideoTest.mIsVideoTestStop = true;
                        LCDTest.LcdTestCase();
                        break;

                    /** 2.MSG_FORNT_CAMERA_TEST: start front camera testcase*/
                    case MSG_FORNT_CAMERA_TEST:
                        LCDTest.mIsLcdTestStop = true;
                        CameraTest.CameraTestCase();
                        break;

                    /**3.MSG_RECEIVER_TEST: start receiver  testcase */
                    case MSG_RECEIVER_TEST:
                        CameraTest.mIsCameraStop = true;
                        ReceiverLoopTest.ReceiverTestCase(); // example 1
                        //ReceiverThreadTest.ReceiverTestCase(); // example 2
                        break;

                    /** 4.MSG_RECORD_TEST: start record testcase */
                    case MSG_RECORD_TEST:
                        ReceiverLoopTest.mIsReceiverTestStop = true;
                        RecordPlayTest.RecordPlayTestCase();
                        break;

                    /** 5.MSG_VIBRATOR_TEST: start vibrator testcase*/
                    case MSG_VIBRATOR_TEST:
                        RecordPlayTest.mIsRecordTestStop = true;
                        VibratorTest.VibratorTestCase();
                        break;

                    /** 6.MSG_RING_TEST: start ring testcase*/
                    case MSG_RING_TEST:
                        VibratorTest.mIsVibratorTestStop = true;
                        RingerTest.RingerTestCase();
                        break;

                    /** 7.MSG_VIDEO_TEST: start videos testcase */
                    case MSG_VIDEO_TEST:
                        RingerTest.mIsRingerTestStop = true;
                        VideoTest.PlayMoviesTestCase();
                        break;
                    /** 8.MSG_UPDATE_STATUS: update the factoryautotest status */
                    case MSG_UPDATE_STATUS:
                        //Initialization  report listview
                        if (mCurrentTestTimes == 1) {
                            ListViewForReport.InitListView();
                        }
                        PassOrFail();//pass or fail
                        //if((mCurrentTestTimes >= 1) && (mCurrentTestTimes <= mALLTestTimes)){
                        ListViewForReport.ViewReport();//update report listview
                        //}
                        stopTest();//Stop Test
                        if (resultCount == mALLTestTimes) {
                            mAllTestResult.setText(Constans.PASS);
                            mAllTestResult.setTextColor(Color.GREEN);
                        } else {
                            mAllTestResult.setText(Constans.FAIL);
                            mAllTestResult.setTextColor(Color.RED);
                        }
                        UpdateCurTimes();// update Test Times
                        break;
                    case MSG_AUTO_TEST:
                        mStartButton.performClick();
                        break;
                    default:
                        break;
                }
            }
        }

    };
    private Toaster toaster;
    private AnrMonitor anrMonitor;


    public void getView() {

        //main RelativeLayout
        mainLayout = (RelativeLayout) findViewById(TEST_MAIN_LAYOUT);

        //LCD test
        mLcdLinearLayout = (LinearLayout) findViewById(TEST_LCD_LAYOUT);

        //Camera test SurfaceView
        mSurfaceView = (SurfaceView) findViewById(TEST_SURFACEVIEW_LAYOUT);

        //video test SurfaceView
        mVideoView = (VideoView) findViewById(TEST_VIDEOVIEW_LAYOUT);

        //Setting times layout
        mSettingLinearLayout = (LinearLayout) findViewById(TEST_SETTING_LAYOUT);

        //EditTestTime
        mEditTestTimesEditText = (EditText) findViewById(TEST_TIMES_ET);

        //Start button
        mStartButton = (Button) findViewById(TEST_START_BT);
        mStartButton.setOnClickListener(mOnClickListener);

        //all and current layout
        mAllTestLinearLayout = (LinearLayout) findViewById(TEST_LIST_LAYOUT);

        //all and current testtimes
        mAllTestTimesTextView = (TextView) findViewById(TEST_ALL_TIMES_TV);
        mCurrentTestTimesTextView = (TextView) findViewById(TEST_CUR_TIMES_TV);

        //ReportListView
        mReportListView = (ListView) findViewById(TEST_REPORT_LAYOUT);

        //Stop button
        mStopButton = (Button) findViewById(TEST_STOP_BT);
        mStopButton.setOnClickListener(mOnClickListener);

        //Report button
        mReportButton = (Button) findViewById(TEST_REPORT_BT);
        mReportButton.setOnClickListener(mOnClickListener);

        mReportListItem = (RelativeLayout) findViewById(TEST_REPORT_ITEM);

        mAllTestResult = (TextView) findViewById(TEST_RESULT_TV);
    }

    /**
     * the  test Items name and color.
     */
    public void SetTestName() {
        mTest1 = (TextView) findViewById(R.id.txt_test1);
        mTest2 = (TextView) findViewById(R.id.txt_test2);
        mTest3 = (TextView) findViewById(R.id.txt_test3);
        mTest4 = (TextView) findViewById(R.id.txt_test4);
        mTest5 = (TextView) findViewById(R.id.txt_test5);
        mTest6 = (TextView) findViewById(R.id.txt_test6);
        mTest7 = (TextView) findViewById(R.id.txt_test7);
        mTest8 = (TextView) findViewById(R.id.txt_test8);
        /************************************************/
        mTest1.setText(R.string.lcd);
        mTest1.setTextColor(Color.BLACK);
        mTest2.setText(R.string.camera_front);
        mTest2.setTextColor(Color.BLACK);
        mTest3.setText(R.string.camera_back);
        mTest3.setTextColor(Color.BLACK);
        mTest4.setText(R.string.str_receiver);
        mTest4.setTextColor(Color.BLACK);
        mTest5.setText(R.string.recordtest);
        mTest5.setTextColor(Color.BLACK);
        mTest6.setText(R.string.vibrator);
        mTest6.setTextColor(Color.BLACK);
        mTest7.setText(R.string.ringer);
        mTest7.setTextColor(Color.BLACK);
        mTest8.setText(R.string.movies);
        mTest8.setTextColor(Color.BLACK);
    }

    /**
     * @Fields: mOnClickListener
     * @Description: Button clicked listener:
     * <br>StartButton-><code>OnStartButton()</code>
     * <br>StopButton-><code>ExitDialog()</code>
     * <br>ReportButton-><code>OnReportButton()</code>
     */
    OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.but_start: //start button
                    OnStartButton();
                    break;
                case R.id.but_stop: //stop button
                    ExitDialog();
                    break;
                case R.id.but_report: //report button
                    OnReportButton();
                    break;
            }
        }
    };

    /**
     * CHB add
     * Forbid return button
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //ExitDialog();

        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        factoryAutoTest = this;

        /**set Window */
        Window window = getWindow();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_factory_test);

        //View view = findViewById(R.layout.activity_factory_test);
        View currentView = getWindow().getDecorView();
        currentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN); // forbid status bar pull-down  SYSTEM_UI_FLAG_IMMERSIVE_GESTURE_ISOLATED

        getView();
        SetTestName();
        /**Register broadcast filters*/
        IntentFilter intentBatteryFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mBatteryReceiver, intentBatteryFilter);

        resultCount = 0;

    }

    /***/
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action) && action.equals(Constans.actionAutoTest)) {
                TEST_AUTO_FLAG = true;
                mEditTestTimesEditText.setText(Constans.DEFAULT_TEST_TIMES);
                mEditTestTimesEditText.setEnabled(false);
                mEditTestTimesEditText.setFocusable(false);
                Toaster.showToast(this, "3秒后，将进入工厂自动化测试！");

                handler.sendEmptyMessageDelayed(MsgTest.MSG_AUTO_TEST, 3000);
                Logger.d("3s later will enter factory auto test!");
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * End for recycling
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        TEST_OVER_FLAG = true;
        LCDTest.mIsLcdTestStop = true;
        CameraTest.mIsCameraStop = true;
        ReceiverLoopTest.mIsReceiverTestStop = true;
        ReceiverThreadTest.mIsReceiverThreadTestStop = true;
        RecordPlayTest.mIsRecordTestStop = true;
        VibratorTest.mIsVibratorTestStop = true;
        RingerTest.mIsRingerTestStop = true;
        VideoTest.mIsVideoTestStop = true;
        if (ReceiverLoopTest.mAudioManager != null) {
            ReceiverLoopTest.mAudioManager.setParameters("SET_LOOPBACK_TYPE=0");
        }
        if (ReceiverThreadTest.audioTrack != null) {
            ReceiverThreadTest.audioTrack.stop();
            ReceiverThreadTest.audioRecord.stop();
        }
        if (RecordPlayTest.isrecorder) {
            if (RecordPlayTest.file != null) {
                if (RecordPlayTest.file.exists()) {
                    RecordPlayTest.file.delete();
                }
                RecordPlayTest.recorder.stop();
                RecordPlayTest.recorder.release();
            }
        }
        if (RecordPlayTest.isplaying) {
            RecordPlayTest.media.stop();
            RecordPlayTest.media.reset();
            RecordPlayTest.recorder = null;
            if (RecordPlayTest.file.exists()) {
                RecordPlayTest.file.delete();
            }
        }
        if (VibratorTest.mTipHelper != null) {
            VibratorTest.mTipHelper.cancel();
        }
        if (RingerTest.mMediaPlayer != null) {
            RingerTest.mMediaPlayer.stop();
        }
        if (VideoTest.mVideoView != null) {
            VideoTest.mVideoView.stopPlayback();
        }
        /**unregister broadcast Filters*/
        unregisterReceiver(mBatteryReceiver);

        if (toaster == null || toaster.onBackPressed()) {
            toaster.onBackPressed();
        }

        resultCount = 0;

    }

    /**
     * font color depending on the test item
     */
    public static void TextViewColorChange(TextView textView1, TextView textView2) {
        textView1.setTextColor(Color.BLACK);
        textView2.setTextColor(Color.BLUE);
    }

    /**
     * @return :void
     * @MethodName: setTestItemColor
     * @Functions: When the test is complete, update the test items color.<br>
     * pass->green;<br>
     * fail->red.
     */
    public static void SetTestItemColor() {
        mTest1.setTextColor(Color.GREEN);
        mTest2.setTextColor(Color.GREEN);
        mTest3.setTextColor(Color.GREEN);
        mTest4.setTextColor(Color.GREEN);
        mTest5.setTextColor(Color.GREEN);
        mTest6.setTextColor(Color.GREEN);
        mTest7.setTextColor(Color.GREEN);
        mTest8.setTextColor(Color.GREEN);
    }

    /**
     * @return :void
     * @MethodName: OnStartButton
     * @Functions: When you click the Start button to begin the following events.
     */
    public void OnStartButton() {
        if (TEST_START_FLAG || TEST_START_FLAG_CHARGEING || TEST_AUTO_FLAG) {//chb modify for VFOZBAUBQ-179 2015-11-9
            KeyBoardUtils.closeKeybord(mEditTestTimesEditText, mContext);//close keypad
            mCurrentTestTimes = 0;
            TEST_OVER_FLAG = false;

            mSettingLinearLayout.setVisibility(View.GONE);
            mAllTestLinearLayout.setVisibility(View.VISIBLE);
            mReportButton.setVisibility(View.VISIBLE);
            //mStopButton.setVisibility(View.VISIBLE);
            mStopButton.setText(R.string.str_stop);
            if (TEST_AUTO_FLAG) {
                mStopButton.setClickable(false);
            }
            /**get the set test times to view*/
            String allTestTimes = mEditTestTimesEditText.getText().toString();
            mAllTestTimesTextView.setText(allTestTimes);
            if (allTestTimes != null) {
                mALLTestTimes = Integer.parseInt(allTestTimes);
            }


            /**start first test item LCD test*/
            factoryAutoTest.handler.sendEmptyMessage(MSG_LCD_TEST);
            UpdateCurTimes();
        } else {
            Toaster.showToast(mContext, mContext.getString(R.string.test_start_tip));
        }
    }

    /**
     * @return :void
     * @MethodName: OnReportButton
     * @Functions:Display Report list.
     */
    public void OnReportButton() {
        if (!IS_REPORT_FLAG) {
            mAllTestLinearLayout.setVisibility(View.INVISIBLE);
            mReportListView.setVisibility(View.VISIBLE);
            mReportListItem.setVisibility(View.VISIBLE);
            mReportButton.setText(R.string.str_reportclose);
            IS_REPORT_FLAG = true;
        } else {
            mReportButton.setText(R.string.str_report);
            mReportListView.setVisibility(View.INVISIBLE);
            mReportListItem.setVisibility(View.INVISIBLE);
            mAllTestLinearLayout.setVisibility(View.VISIBLE);
            IS_REPORT_FLAG = false;
        }
    }

    /**
     * @return :void
     * @MethodName: UpdateCurTimes
     * @Functions:Each test time, plus a number of tests
     */
    public void UpdateCurTimes() {
        if (mCurrentTestTimes < mALLTestTimes) {
            mCurrentTestTimes = mCurrentTestTimes + 1;
            mCurrentTestTimesTextView.setText(mCurrentTestTimes + "");
        }
    }

    /**
     * @return :void
     * @MethodName: stopTest
     * @Functions: End of the test,do the following events
     */
    public void stopTest() {
        if (mCurrentTestTimes == mALLTestTimes) {
            TEST_OVER_FLAG = true;
            Toaster.showToast(factoryAutoTest, FactoryAutoTest.mContext.getString(R.string.str_test_over));
            mTest8.setTextColor(Color.BLACK);
            mStopButton.setText(R.string.str_test_stop);

            mStopButton.setClickable(true);
            mStopButton.setTextColor(Color.GREEN);
            mAllTestResult.setVisibility(View.VISIBLE);
            SetTestItemColor();
            ChangeReportListColor();
        }
    }

    /**
     * @return :void
     * @MethodName: ExitDialog
     * @Functions:When exit the program, pop-up dialog.
     */
    public void ExitDialog() {
        toaster = new Toaster.Builder(FactoryAutoTest.this)
                .setAnimationDuration(Constans.DURATION_DIALOG)
                .setText(mContext.getString(R.string.str_exit))
                .setTitle(mContext.getString(R.string.notice))
                .setNegative(mContext.getString(R.string.str_cancel))
                .setPositive(mContext.getString(R.string.str_OK))
                .setCallBack(this)
                .build();
        toaster.show();
    }

    /**
     * @return :void
     * @MethodName: PassOrFail
     * @Functions:Determine whether the test pass or fail.
     * <br>If pass, <code>mTestResult</code>= "Pass";<br>or not <code>mTestResult</code>= "Fail".
     */
    public void PassOrFail() {
        /************测试结果记录****************/
        if ((getAllTestTime() < FactoryAutoTest.mSetAllTestTime) // Within the allowable time range
//                && !(CameraTest.mFrontCameraTestTime < 4) // Front Camera  Exist!
                && !(CameraTest.mBackCameraTestTime < 4)) { // Back Camera Exist!
            FactoryAutoTest.mTestResult = Constans.PASS;
            FactoryAutoTest.mTestResultColor = 0xff00ff00;//green   //0xff00ff00  green
            resultCount += 1;
        } else {
            FactoryAutoTest.mTestResult = Constans.FAIL;
            FactoryAutoTest.mTestResultColor = 0xffff0000;//red
            FactoryAutoTest.TEST_RESULT_PASS_FLAG = false;
        }
    }

    /**
     * @return :void
     * @MethodName: ChangeReportListColor
     * @Functions:Change the color of the test report list, only one test failure, it becomes red, or green.
     */
    public static void ChangeReportListColor() {
        if (FactoryAutoTest.TEST_RESULT_PASS_FLAG) {
            mReportListView.setBackgroundColor(Color.GREEN); //green
        } else {
            mReportListView.setBackgroundColor(Color.RED); //red
        }
    }

    /**
     * @return :long-><code>mALLTestTime</code>
     * @MethodName: getAllTestTime
     * @Functions:Get all test item time.
     */
    public long getAllTestTime() {
        mALLTestTime = LCDTest.mLCDTestTime
                + CameraTest.mFrontCameraTestTime
                + CameraTest.mBackCameraTestTime
                + ReceiverLoopTest.mReceiverTestTime
                + RecordPlayTest.mRecordTestTime
                + VibratorTest.mVibratorTestTime
                + RingerTest.mRingTestTime
                + VideoTest.mVideoTestTime;
        return mALLTestTime;
    }

    @Override
    public void onPositiveClick() {
        FactoryAutoTest.this.finish();
    }

    @Override
    public void onNegativeClick() {
    }
}
