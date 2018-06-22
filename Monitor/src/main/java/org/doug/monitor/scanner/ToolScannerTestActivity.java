package org.doug.monitor.scanner;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.widget.TextView;

import org.doug.monitor.R;
import org.doug.monitor.base.BaseActivity;
import org.doug.monitor.base.Constans;
import org.doug.monitor.base.circleprogressbar.CountDownView;
import org.doug.monitor.base.util.SharedPreferencesUtils;
import org.doug.monitor.base.util.Toaster;
import org.doug.monitor.base.util.ToolScanner;
import org.doug.monitor.displayVersion.DisplayVersionActivity;

/**
 * Created by wesine on 2018/6/14.
 * 1、和测试条码对比，一次不一致fail
 * 2、测试时间18s内超过8次全通过pass，否则fail
 * 3、测试pass，才可以自动跳转，否则停在测试界面
 */

public class ToolScannerTestActivity extends BaseActivity implements ToolScanner.OnScanSuccessListener, CountDownView.OnTimeCompleteListener {

    private ToolScanner toolScanner;
    private TextView tv_toolscanner;

    private StringBuilder sb;

    private static final String CODE = "0022AF04";
    private static final int LEN = 8;

    private CountDownView cdv_second;

    private static final int MSG_7 = 1106;
    private static final int MSG_8 = 1107;
    private boolean overFlag = false;//测试倒计时flag

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg != null) {
                if (msg.what == MSG_7) {
                    cdv_second.start();
                    Toaster.showToast(ToolScannerTestActivity.this, "测试倒计时18秒！");
                } else if (msg.what == MSG_8) {
                    SharedPreferencesUtils.putToSpfs(ToolScannerTestActivity.this, Constans.TEST_ASSEMBLY_2, 1);
                    SharedPreferencesUtils.putToSpfs(ToolScannerTestActivity.this, Constans.TEST_PERFORMANCE_5, 1);
                    setResult(RESULT_OK);
                    ToolScannerTestActivity.this.finish();
                }
            }
            return true;
        }
    });
    private TextView tv_scanner_result;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolscannertest);
        setTitle("扫码器");
        setBackBtn();
        tv_scanner_result = (TextView) findViewById(R.id.tv_scanner_result);
        cdv_second = (CountDownView) findViewById(R.id.countdown_timer_second);
        cdv_second.initTime(18);
        cdv_second.setOnTimeCompleteListener(this);
        tv_toolscanner = (TextView) findViewById(R.id.tv_toolScanner);
        toolScanner = new ToolScanner(this);
        sb = new StringBuilder();
    }

    @Override
    public void onScanSuccess(String barcode) {
        if (overFlag) {
            return;
        }
        sb.append(barcode + "\n");
        tv_toolscanner.setText(sb.toString());
        if (barcode.equals(CODE)) {

        } else {
            tv_scanner_result.setText("Fail");
            overFlag = true;
        }

    }

    private void checkResult(String result) {
        if (result.length() % LEN == 0 && result.length() / LEN > 8) {
            tv_scanner_result.setText("Pass");
            handler.sendEmptyMessageDelayed(MSG_8, 3000);
        } else {
            tv_scanner_result.setText("Fail");
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        toolScanner.analysisKeyEvent(event);
        return true;
//        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ENTER) {

        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        toolScanner.onDestroy();
    }

    @Override
    public void onTimeComplete() {
        overFlag = true;
        checkResult(sb.toString().replace("\n", ""));
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessageDelayed(MSG_7, 3000);
        Toaster.showToast(this, "3秒后开始测试！");
    }
}
