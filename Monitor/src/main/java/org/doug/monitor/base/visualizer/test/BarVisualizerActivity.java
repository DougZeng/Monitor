package org.doug.monitor.base.visualizer.test;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;

import org.doug.monitor.R;
import org.doug.monitor.base.visualizer.BarVisualizer;

/**
 * Created by wesine on 2018/6/21.
 */

public class BarVisualizerActivity extends BaseActivity {

    @Override
    protected void init() {
        BarVisualizer barVisualizer = findViewById(R.id.visualizer);

        // set custom color to the line.
        barVisualizer.setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        // define custom number of bars you want in the visualizer between (10 - 256).
        barVisualizer.setDensity(70);

        // Set your media player to the visualizer.
        barVisualizer.setPlayer(mediaPlayer.getAudioSessionId());
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
        return R.layout.activity_bar_visualizer;
    }
}
