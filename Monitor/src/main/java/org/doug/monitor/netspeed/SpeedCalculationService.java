package org.doug.monitor.netspeed;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.orhanobut.logger.Logger;

import org.doug.monitor.base.Constans;
import org.doug.monitor.base.util.SharedPreferencesUtils;

/**
 * Created by wesine on 2018/5/23.
 */

public class SpeedCalculationService extends Service {
    private WindowUtil windowUtil;
    private boolean changed = false;

    @Override
    public void onCreate() {
        super.onCreate();
        WindowUtil.initX = (int) SharedPreferencesUtils.getFromSpfs(this, Constans.INIT_X, 0);
        WindowUtil.initY = (int) SharedPreferencesUtils.getFromSpfs(this, Constans.INIT_Y, 0);
        windowUtil = new WindowUtil(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        changed = intent.getBooleanExtra(Constans.CHANGED, false);
        if (changed) {
            windowUtil.onSettingChanged();
        } else {
            if (!windowUtil.isShowing()) {
                windowUtil.showSpeedView();
            }
            SharedPreferencesUtils.putToSpfs(this, Constans.IS_SHOWN, true);
        }
        //return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WindowManager.LayoutParams params = (WindowManager.LayoutParams) windowUtil.speedView.getLayoutParams();
        SharedPreferencesUtils.putToSpfs(this, Constans.INIT_X, params.x);
        SharedPreferencesUtils.putToSpfs(this, Constans.INIT_Y, params.y);
        if (windowUtil.isShowing()) {
            windowUtil.closeSpeedView();
            SharedPreferencesUtils.putToSpfs(this, Constans.IS_SHOWN, false);
        }
        Logger.d("service destroy");
    }
}
