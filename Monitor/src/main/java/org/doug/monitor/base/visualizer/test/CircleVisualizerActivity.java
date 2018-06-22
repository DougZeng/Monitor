package org.doug.monitor.base.visualizer.test;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;

import org.doug.monitor.R;
import org.doug.monitor.base.visualizer.CircleVisualizer;

/**
 * Created by wesine on 2018/6/21.
 */

public class CircleVisualizerActivity extends BaseActivity {

    @Override
    protected void init() {
        CircleVisualizer circleVisualizer = findViewById(R.id.visualizer);
        // set custom color to the line.
        circleVisualizer.setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        // Customize the size of the circle. by defalut multipliers is 1.
        circleVisualizer.setRadiusMultiplier(2f);

        // set the line with for the visualizer between 1-10 default 1.
        circleVisualizer.setStrokeWidth(1);

        // Set your media player to the visualizer.
        circleVisualizer.setPlayer(mediaPlayer.getAudioSessionId());
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
        return R.layout.activity_circle_visualizer;
    }
}
