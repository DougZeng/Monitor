package org.doug.monitor.serialNo;

import android.os.Bundle;
import android.widget.TextView;

import org.doug.monitor.R;
import org.doug.monitor.base.BaseActivity;
import org.doug.monitor.base.Constans;
import org.doug.monitor.base.circleprogressbar.CountDownView;
import org.doug.monitor.base.util.DeviceUtil;
import org.doug.monitor.base.util.SharedPreferencesUtils;
import org.doug.monitor.displayVersion.DisplayVersionActivity;

/**
 * Created by wesine on 2018/6/20.
 */

public class SerialNoActivity extends BaseActivity implements CountDownView.OnTimeCompleteListener {

    private CountDownView cdv_second;
    private TextView tv_serial_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serialno);
        setTitle("序列码");
        setBackBtn();

        cdv_second = (CountDownView) findViewById(R.id.countdown_timer_second);
        tv_serial_no = (TextView) findViewById(R.id.tv_serial_no);
        cdv_second.initTime(15);
        cdv_second.setOnTimeCompleteListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cdv_second.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cdv_second.start();
        tv_serial_no.setText(DeviceUtil.getSerial());
    }


    @Override
    public void onTimeComplete() {
        SharedPreferencesUtils.putToSpfs(SerialNoActivity.this, Constans.TEST_PERFORMANCE_1, 1);
        setResult(RESULT_OK);
        this.finish();
    }
}
