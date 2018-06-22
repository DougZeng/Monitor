package org.doug.monitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import org.doug.monitor.base.util.ScreenUtils;
import org.doug.monitor.menu.Menu;

/**
 *
 * Created by wesine on 2018/6/14.
 */

public class FlashActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.hideBottomUIMenu(this);
        setContentView(R.layout.activity_flash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(FlashActivity.this, Menu.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

}
