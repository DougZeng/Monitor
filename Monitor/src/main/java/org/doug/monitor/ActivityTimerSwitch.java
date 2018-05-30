package org.doug.monitor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.orhanobut.logger.Logger;

import org.doug.monitor.util.DateUtil;
import org.doug.monitor.util.Toaster;

/**
 * Created by wesine on 2018/5/28.
 */

public class ActivityTimerSwitch extends AppCompatActivity implements View.OnClickListener {

    private Button btn_power_on;
    private Button btn_cancel_off;
    private Button btn_power_off;
    private TextInputEditText ipt_on_date;
    private TextInputEditText ipt_on_time;
    private TextInputEditText ipt_off_date;
    private TextInputEditText ipt_off_time;

    ZYTimerSwitch timerSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_switch);

        btn_power_on = (Button) findViewById(R.id.btn_power_on);
        btn_power_off = (Button) findViewById(R.id.btn_power_off);
        btn_cancel_off = (Button) findViewById(R.id.btn_cancel_off);
        btn_power_on.setOnClickListener(this);
        btn_power_off.setOnClickListener(this);
        btn_cancel_off.setOnClickListener(this);

        ipt_on_date = (TextInputEditText) findViewById(R.id.ipt_on_date);
        ipt_on_time = (TextInputEditText) findViewById(R.id.ipt_on_time);
        ipt_off_date = (TextInputEditText) findViewById(R.id.ipt_off_date);
        ipt_off_time = (TextInputEditText) findViewById(R.id.ipt_off_time);

        ipt_on_date.setText(DateUtil.getCurrentDate());
        ipt_on_time.setText(DateUtil.getCurrentTime());
        ipt_off_date.setText(DateUtil.getCurrentDate());
        ipt_off_time.setText(DateUtil.getCurrentTime());

        //TODO 众云
        timerSwitch = new ZYTimerSwitch(this);
        String currentDate = DateUtil.getCurrentDate();
        Logger.d(currentDate);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_power_on:
                String on_date = ipt_on_date.getText().toString().trim();
                String on_time = ipt_on_time.getText().toString().trim();
                if (!TextUtils.isEmpty(on_date) && !TextUtils.isEmpty(on_time)) {
                    timerSwitch.poweron(on_date, on_time);
                } else {
                    Toaster.showToast(this, "请重新设置开机时间！");
                }

                break;

            case R.id.btn_power_off:
                String off_date = ipt_off_date.getText().toString().trim();
                String off_time = ipt_off_time.getText().toString().trim();
                if (!TextUtils.isEmpty(off_date) && !TextUtils.isEmpty(off_time)) {
                    timerSwitch.poweroff(off_date, off_time);
                } else {
                    Toaster.showToast(this, "请重新设置关机时间！");
                }
                break;

            case R.id.btn_cancel_off:
                timerSwitch.cancelPowerOff();
                break;
        }
    }
}
