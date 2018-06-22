package org.doug.monitor.displayVersion;

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

public class DisplayVersionActivity extends BaseActivity implements CountDownView.OnTimeCompleteListener {


    private CountDownView cdv_second;
    private TextView tv_displayVersion;
    private static final int MSG_4 = 1103;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg != null) {
                if (msg.what == MSG_4) {
                    cdv_second.start();
                    Toaster.showToast(DisplayVersionActivity.this, "测试倒计时15秒！");
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
        tv_displayVersion.setText(DeviceUtil.getDisplayVersion());
        Toaster.showToast(this, "3秒后开始测试！");
        handler.sendEmptyMessageDelayed(MSG_4, 3000);
    }


    @Override
    public void onTimeComplete() {
        SharedPreferencesUtils.putToSpfs(DisplayVersionActivity.this, Constans.TEST_PERFORMANCE_0, 1);
        setResult(RESULT_OK);
        this.finish();
    }
}
