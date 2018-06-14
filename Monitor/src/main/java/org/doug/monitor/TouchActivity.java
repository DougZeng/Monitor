package org.doug.monitor;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;

import org.doug.monitor.base.util.TouchSurfaceView;


/**
 * Created by wesine on 2018/6/14.
 */

public class TouchActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);
        TouchSurfaceView touchSurfaceView = new TouchSurfaceView(this);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fl_main);
        frameLayout.addView(touchSurfaceView);

    }
}
