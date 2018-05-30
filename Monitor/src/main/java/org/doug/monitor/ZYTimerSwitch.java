package org.doug.monitor;

import android.content.Context;
import android.content.Intent;

import com.orhanobut.logger.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wesine on 2018/5/24.
 */

public class ZYTimerSwitch {
    private Context mContext;

    /**
     * 注意事项：
     * 1、接收开机广播先执行取消定时关机命令，再设置下次关机时间，关机时间距离当前系统时间至少5分钟
     * 2、接收到关机广播设置下次开机时间，开机时间距离当前系统时间至少5分钟
     */

    public ZYTimerSwitch(Context context) {
        this.mContext = context;
    }

    /**
     * 设置定时开机
     */
    public void poweron() {
        Intent intent = new Intent("zysd.alarm.poweron.time");
        intent.putExtra("poweronday", "2014-06-11");
        intent.putExtra("powerontime", "23:59");
        mContext.sendBroadcast(intent);
    }

    /**
     * 设置定时开机
     */
    public void poweron(String on_date, String on_time) {
        Intent intent = new Intent("zysd.alarm.poweron.time");
        intent.putExtra("poweronday", on_date);
        intent.putExtra("powerontime", on_time);
        Logger.d("power on %s", on_date + " " + on_time);
        mContext.sendBroadcast(intent);
    }

    /**
     * 设置定时关机
     */
    public void poweroff() {
        Intent intent = new Intent("zysd.alarm.poweroff.time");
        intent.putExtra("poweroffday", "2014-06-11");
        intent.putExtra("powerofftime", "21:18");
        mContext.sendBroadcast(intent);
    }

    /**
     * 设置定时关机
     */
    public void poweroff(String off_date, String off_time) {
        Intent intent = new Intent("zysd.alarm.poweroff.time");
        intent.putExtra("poweroffday", off_date);
        intent.putExtra("powerofftime", off_time);
        Logger.d("power off %s", off_date + " " + off_time);
        mContext.sendBroadcast(intent);
    }

    /**
     * 取消定时关机命令
     */
    public void cancelPowerOff() {
        Intent intent = new Intent("zysd.alarm.poweroff.cancel");
        mContext.sendBroadcast(intent);
    }

}
