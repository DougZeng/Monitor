package org.doug.monitor.base.util;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.TextView;

import org.doug.monitor.R;

/**
 * Created by wesine on 2018/6/14.
 */

public class ToolScannerTestActivity extends Activity implements ToolScanner.OnScanSuccessListener {

    private ToolScanner toolScanner;
    private TextView tv_toolscanner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolscannertest);
        tv_toolscanner = (TextView) findViewById(R.id.tv_toolScanner);
        toolScanner = new ToolScanner(this);
    }

    @Override
    public void onScanSuccess(String barcode) {
        if (TextUtils.isEmpty(barcode)) {
            Toaster.showToast(this, "条码为空！");
            return;
        }
        tv_toolscanner.setText(barcode);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        toolScanner.analysisKeyEvent(event);
        return true;
//        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        toolScanner.onDestroy();
    }
}
