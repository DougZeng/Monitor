package org.doug.monitor.touch;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;

import org.doug.monitor.R;
import org.doug.monitor.base.BaseActivity;
import org.doug.monitor.base.Constans;
import org.doug.monitor.base.circleprogressbar.CountDownView;
import org.doug.monitor.base.util.SharedPreferencesUtils;
import org.doug.monitor.base.util.Toaster;
import org.doug.monitor.base.util.TouchSurfaceView;
import org.doug.monitor.scanner.ToolScannerTestActivity;


/**
 * Created by wesine on 2018/6/14.
 * 1、30秒测试时间后，弹窗确认是否通过测试
 * 2、通过测试Pass，自动关闭当前界面
 * 3、Fail则停在该界面
 */

public class TouchActivity extends BaseActivity implements CountDownView.OnTimeCompleteListener, Toaster.DialogCallback {

    private CountDownView cdv_second;
    private static final int MSG_9 = 1108;
    private static final int MSG_10 = 1109;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg != null) {
                if (msg.what == MSG_9) {
                    cdv_second.start();
                    Toaster.showToast(TouchActivity.this, "测试倒计时30秒！");
                } else if (msg.what == MSG_10) {
                    SharedPreferencesUtils.putToSpfs(TouchActivity.this, Constans.TEST_ASSEMBLY_3, 1);
                    SharedPreferencesUtils.putToSpfs(TouchActivity.this, Constans.TEST_ASSEMBLY_4, 1);
                    SharedPreferencesUtils.putToSpfs(TouchActivity.this, Constans.TEST_PERFORMANCE_6, 1);
                    SharedPreferencesUtils.putToSpfs(TouchActivity.this, Constans.TEST_PERFORMANCE_7, 1);
                    setResult(RESULT_OK);
                    TouchActivity.this.finish();
                }
            }
            return true;
        }
    });
    private Toaster toaster;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);
        setTitle("触摸测试");
        setBackBtn();
        cdv_second = (CountDownView) findViewById(R.id.countdown_timer_second);
        cdv_second.initTime(30);
        cdv_second.setOnTimeCompleteListener(this);
        TouchSurfaceView touchSurfaceView = new TouchSurfaceView(this);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fl_main);
        frameLayout.addView(touchSurfaceView);
    }


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
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessageDelayed(MSG_9, 3000);
        Toaster.showToast(this, "3秒后开始测试！");
    }

    @Override
    public void onPositiveClick() {
        Toaster.showToast(this, "Pass");
        handler.sendEmptyMessageDelayed(MSG_10, 1000);
    }

    @Override
    public void onNegativeClick() {
        Toaster.showToast(this, "Fail");
    }
}
