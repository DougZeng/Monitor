package org.doug.monitor.netspeed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import org.doug.monitor.Constans;

/**
 * Created by wesine on 2018/5/23.
 */

public class BootCompletedBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            return;
        }
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            Logger.d("receiver receive boot broadcast");
            boolean isShown = (boolean) SharedPreferencesUtils.getFromSpfs(context, Constans.IS_SHOWN, false);
            if (isShown) {
                context.startService(new Intent(context, SpeedCalculationService.class));
            }
        }
    }
}
