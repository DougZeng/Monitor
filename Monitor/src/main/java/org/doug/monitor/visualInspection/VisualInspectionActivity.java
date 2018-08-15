package org.doug.monitor.visualInspection;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.CompoundButton;

import org.doug.monitor.R;
import org.doug.monitor.base.BaseActivity;
import org.doug.monitor.base.Constans;
import org.doug.monitor.base.circleprogressbar.CountDownView;
import org.doug.monitor.base.switchbutton.SwitchButton;
import org.doug.monitor.base.util.DeviceUtil;
import org.doug.monitor.base.util.SharedPreferencesUtils;
import org.doug.monitor.base.util.Toaster;

/**
 * Created by wesine on 2018/6/20.
 * <p>
 * 手动切换pass判断结果
 */

public class VisualInspectionActivity extends BaseActivity implements CountDownView.OnTimeCompleteListener, CompoundButton.OnCheckedChangeListener {
    private CountDownView cdv_second;
    private SwitchButton switchButton;

    private static final int MSG_3 = 1102;

    private String visualInspectionMode = "";

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg != null) {
                if (msg.what == MSG_3) {
                    cdv_second.start();
                    Toaster.showToast(VisualInspectionActivity.this, "测试倒计时" + Constans.TEST_TIME_VISUAL + "秒！");

                }
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_inspection);
        setTitle("外观检测");
        setBackBtn();

        switchButton = (SwitchButton) findViewById(R.id.sb_text_state);
        switchButton.setOnCheckedChangeListener(this);
        cdv_second = (CountDownView) findViewById(R.id.countdown_timer_second);
        cdv_second.initTime(Constans.TEST_TIME_VISUAL);
        cdv_second.setOnTimeCompleteListener(this);

        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action)) {
                visualInspectionMode = action;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cdv_second.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessageDelayed(MSG_3, Constans.DELAYMILLIS);
    }


    @Override
    public void onTimeComplete() {
        Toaster.showToast(this, "请选择是否通过外观检测？");
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (visualInspectionMode.equals(Constans.ACTION_A + 0)) {
                SharedPreferencesUtils.putToSpfs(this, Constans.TEST_ASSEMBLY_0, Constans.PASS);
            } else if (visualInspectionMode.equals(Constans.ACTION_P + 4)) {
                SharedPreferencesUtils.putToSpfs(this, Constans.TEST_PERFORMANCE_4, Constans.PASS);
            }
            setResult(RESULT_OK);
            this.finish();
        }
    }
}
