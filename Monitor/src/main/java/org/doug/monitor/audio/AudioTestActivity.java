package org.doug.monitor.audio;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;

import com.orhanobut.logger.Logger;

import org.doug.monitor.R;
import org.doug.monitor.base.BaseActivity;
import org.doug.monitor.base.Constans;
import org.doug.monitor.base.circleprogressbar.CountDownView;
import org.doug.monitor.base.util.SharedPreferencesUtils;
import org.doug.monitor.base.visualizer.BarVisualizer;
import org.doug.monitor.base.visualizer.test.MediaPlayerService;

/**
 * Created by wesine on 2018/6/20.
 */

public class AudioTestActivity extends BaseActivity implements CountDownView.OnTimeCompleteListener {

    private static final int MSG_1 = 1100;

    private MediaPlayerService mBoundService;
    private BarVisualizer barVisualizer;
    private CountDownView cdv_second;
    private ImageButton ib_play_pause;
    private ImageButton ib_replay;

    private String audioMode = "";

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg != null) {
                int what = msg.what;
                switch (what) {
                    case MSG_1:
                        ib_play_pause.performClick();
                        break;
                }
            }
            return true;
        }
    });

    private AudioManager mAudioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_test);
        setTitle("喇叭测试");
//        setBackBtn();

        initView();

        initListener();

        initialize();


        initVolume();


    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action)) {
                audioMode = action;
            }
        }
    }

    private void initVolume() {
        //TODO 自动把声音调节最大
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        }
        try {
            int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            Logger.d("currentVolume %d,maxVolume %d", currentVolume, maxVolume);
            if (currentVolume < maxVolume) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initListener() {
        ib_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cdv_second.start();
                playPauseBtnClicked((ImageButton) view);
            }
        });

        ib_replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoundService.replay();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // register LocalBroadcastManager
        LocalBroadcastManager.getInstance(this).registerReceiver(bReceiver, new IntentFilter(MediaPlayerService.INTENT_FILTER));
        Intent intent = new Intent(this, MediaPlayerService.class);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessageDelayed(MSG_1, 100);
    }

    private void initialize() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(org.doug.monitor.base.visualizer.test.BaseActivity.WRITE_EXTERNAL_STORAGE_PERMS, org.doug.monitor.base.visualizer.test.BaseActivity.AUDIO_PERMISSION_REQUEST_CODE);
        }
    }

    private void initView() {
        barVisualizer = findViewById(R.id.visualizer);
        // set custom color to the line.
        barVisualizer.setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        // define custom number of bars you want in the visualizer between (10 - 256).
        barVisualizer.setDensity(70);

        cdv_second = (CountDownView) findViewById(R.id.countdown_timer_second);
        cdv_second.initTime(Constans.TEST_TIME_AUDIO);
        cdv_second.setOnTimeCompleteListener(this);

        ib_play_pause = (ImageButton) findViewById(R.id.ib_play_pause);
        ib_replay = (ImageButton) findViewById(R.id.ib_replay);
    }

    @Override
    public void onTimeComplete() {
        ib_play_pause.performClick();
        if (audioMode.equals(Constans.ACTION_A + 4)) {
            SharedPreferencesUtils.putToSpfs(AudioTestActivity.this, Constans.TEST_ASSEMBLY_4, Constans.PASS);
        } else if (audioMode.equals(Constans.ACTION_P + 8)) {
            SharedPreferencesUtils.putToSpfs(AudioTestActivity.this, Constans.TEST_PERFORMANCE_8, Constans.PASS);
        }
        setResult(RESULT_OK);
        this.finish();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        switch (requestCode) {
            case org.doug.monitor.base.visualizer.test.BaseActivity.AUDIO_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    finish();
                }
        }
    }


    public void replay(View view) {
        mBoundService.replay();
    }

    public void playPause(View view) {
        playPauseBtnClicked((ImageButton) view);
    }

    /**
     * receive audio session id required for visualizer through
     * broadcast receiver from service
     * ref https://stackoverflow.com/a/27652660/5164673
     */
    private BroadcastReceiver bReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int audioSessionId = intent.getIntExtra(MediaPlayerService.INTENT_AUDIO_SESSION_ID, -1);
            if (audioSessionId != -1) {
                barVisualizer.setPlayer(audioSessionId);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, MediaPlayerService.class));
        unbindService(serviceConnection);
    }

    protected void onPause() {
        super.onPause();
        // unregister LocalBroadcastManager
        LocalBroadcastManager.getInstance(this).unregisterReceiver(bReceiver);
        cdv_second.onPause();
    }

    public void playPauseBtnClicked(ImageButton btnPlayPause) {
        if (mBoundService.isPlaying()) {
            mBoundService.pause();
            btnPlayPause.setImageDrawable(ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_play_red_48dp));
        } else {
            mBoundService.start();
            btnPlayPause.setImageDrawable(ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_pause_red_48dp));
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerService.MediaPlayerServiceBinder myBinder = (MediaPlayerService.MediaPlayerServiceBinder) service;
            mBoundService = myBinder.getService();
        }
    };

//    public void line(View view) {
//        startActivity(LineVisualizerActivity.class);
//    }
//
//    public void bar(View view) {
//        startActivity(BarVisualizerActivity.class);
//    }
//
//    public void circle(View view) {
//        startActivity(CircleVisualizerActivity.class);
//    }
//
//    public void circleBar(View view) {
//        startActivity(CircleBarVisualizerActivity.class);
//    }
//
//    public void lineBar(View view) {
//        startActivity(LineBarVisualizerActivity.class);
//    }
//
//    public void service(View view) {
//        startActivity(ServiceExampleActivity.class);
//    }
//
//    public void startActivity(Class clazz) {
//        startActivity(new Intent(this, clazz));
//    }
}
