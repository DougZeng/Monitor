package org.doug.monitor.base.visualizer.test;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;

import org.doug.monitor.R;
import org.doug.monitor.base.visualizer.CircleBarVisualizer;

/**
 * Created by wesine on 2018/6/21.
 */

public class CircleBarVisualizerActivity extends BaseActivity {

    @Override
    protected void init() {
        CircleBarVisualizer circleBarVisualizer = findViewById(R.id.visualizer);
        circleBarVisualizer.setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        circleBarVisualizer.setPlayer(mediaPlayer.getAudioSessionId());
    }

    public void replay(View view) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(0);
        }
    }

    public void playPause(View view) {
        playPauseBtnClicked((ImageButton) view);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_circle_bar_visualizer;
    }
}
