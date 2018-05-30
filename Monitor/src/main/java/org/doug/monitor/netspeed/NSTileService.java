package org.doug.monitor.netspeed;

import android.content.Intent;
import android.os.Build;
import android.service.quicksettings.TileService;
import android.support.annotation.RequiresApi;

/**
 * Created by wesine on 2018/5/23.
 */

@RequiresApi(api = Build.VERSION_CODES.N)
public class NSTileService extends TileService {
    @Override
    public void onClick() {
        super.onClick();
        if (WindowUtil.isShowing) {
            stopService(new Intent(this, SpeedCalculationService.class));
        } else {
            startService(new Intent(this, SpeedCalculationService.class));
        }

    }
}
