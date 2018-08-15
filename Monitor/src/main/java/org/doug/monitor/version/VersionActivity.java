package org.doug.monitor.version;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import org.doug.monitor.R;
import org.doug.monitor.base.BaseActivity;
import org.doug.monitor.base.Constans;
import org.doug.monitor.base.circleprogressbar.CountDownView;
import org.doug.monitor.base.util.DeviceUtil;
import org.doug.monitor.base.util.SharedPreferencesUtils;
import org.doug.monitor.base.util.Toaster;

/**
 * Created by wesine on 2018/6/20.
 */

public class VersionActivity extends BaseActivity implements CountDownView.OnTimeCompleteListener, Toaster.DialogCallback {


    private CountDownView cdv_second;
    private TextView tv_displayVersion;
    private TextView tv_serial_no;
    private TextView tv_eth_mac;
    private static final int MSG_3 = 1103;
    private static final int MSG_4 = 1104;
    private static final int MSG_5 = 1105;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg != null) {
                if (msg.what == MSG_3) {
                    cdv_second.start();
                    Toaster.showToast(VersionActivity.this, "测试倒计时" + Constans.TEST_TIME_VERSION + "秒！");
                } else if (msg.what == MSG_4) {
                    SharedPreferencesUtils.putToSpfs(VersionActivity.this, Constans.TEST_PERFORMANCE_0, Constans.PASS);
                    SharedPreferencesUtils.putToSpfs(VersionActivity.this, Constans.TEST_PERFORMANCE_1, Constans.PASS);
                    SharedPreferencesUtils.putToSpfs(VersionActivity.this, Constans.TEST_PERFORMANCE_2, Constans.PASS);
                    SharedPreferencesUtils.putToSpfs(VersionActivity.this, Constans.TEST_PERFORMANCE_3, Constans.PASS);
                    setResult(RESULT_OK);
                    VersionActivity.this.finish();
                } else if (msg.what == MSG_5) {
                    SharedPreferencesUtils.putToSpfs(VersionActivity.this, Constans.TEST_PERFORMANCE_0, Constans.FAIL);
                    SharedPreferencesUtils.putToSpfs(VersionActivity.this, Constans.TEST_PERFORMANCE_1, Constans.FAIL);
                    SharedPreferencesUtils.putToSpfs(VersionActivity.this, Constans.TEST_PERFORMANCE_2, Constans.FAIL);
                    SharedPreferencesUtils.putToSpfs(VersionActivity.this, Constans.TEST_PERFORMANCE_3, Constans.FAIL);
                }
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_version);
        setTitle("软件版本");
        setBackBtn();
        cdv_second = (CountDownView) findViewById(R.id.countdown_timer_second);
        tv_displayVersion = (TextView) findViewById(R.id.tv_displayVersion);
        tv_serial_no = (TextView) findViewById(R.id.tv_serial_no);
        tv_eth_mac = (TextView) findViewById(R.id.tv_eth_mac);
        cdv_second.initTime(Constans.TEST_TIME_VERSION);
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
        tv_displayVersion.setText(DeviceUtil.getDisplayVersion());
        tv_serial_no.setText(DeviceUtil.getSerial());
        tv_eth_mac.setText("以太网Mac地址: " + DeviceUtil.getEth0() + "\n" + "WIFI Mac地址: " + DeviceUtil.getWifiMac(this));
//        Toaster.showToast(this, "3秒后开始测试！");
        handler.sendEmptyMessageDelayed(MSG_3, Constans.DELAYMILLIS);
    }


    private Toaster toaster;

    @Override
    public void onTimeComplete() {

        toaster = new Toaster.Builder(this)
                .setTitle("触摸测试是否通过？")
                .setPositive("是")
                .setNegative("否")
                .setCallBack(this)
                .build();
        toaster.show();


    }

    @Override
    public void onPositiveClick() {
        handler.sendEmptyMessage(MSG_4);
    }

    @Override
    public void onNegativeClick() {
        handler.sendEmptyMessage(MSG_5);
    }
}
