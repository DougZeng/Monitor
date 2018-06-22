package org.doug.monitor.eth;

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

public class EthMacActivity extends BaseActivity implements CountDownView.OnTimeCompleteListener {
    private TextView tv_eth_mac;
    private CountDownView cdv_second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eth_mac);
        setTitle("有线MAC地址");
        setBackBtn();
        tv_eth_mac = (TextView) findViewById(R.id.tv_eth_mac);
        cdv_second = (CountDownView) findViewById(R.id.countdown_timer_second);
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
        tv_eth_mac.setText("以太网Mac地址: " + DeviceUtil.getEth0() + "\n" + "WIFI Mac地址: " + DeviceUtil.getWifiMac(this));
    }


    @Override
    public void onTimeComplete() {
        SharedPreferencesUtils.putToSpfs(EthMacActivity.this, Constans.TEST_PERFORMANCE_2, 1);
        setResult(RESULT_OK);
        this.finish();
    }

}
