package org.doug.monitor.menu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

import org.doug.monitor.R;
import org.doug.monitor.ageing.Ageing;
import org.doug.monitor.assembly.Assembly;
import org.doug.monitor.assembly.AssemblyTestActivity;
import org.doug.monitor.base.App;
import org.doug.monitor.base.Constans;
import org.doug.monitor.base.util.DeviceUtil;
import org.doug.monitor.base.util.GenerateUtil;
import org.doug.monitor.base.util.ScreenUtils;
import org.doug.monitor.base.util.SharedPreferencesUtils;
import org.doug.monitor.clean.CleanDataActivity;
import org.doug.monitor.factorytest.FactoryAutoTest;
import org.doug.monitor.performance.Performance;
import org.doug.monitor.performance.PerformanceTestActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wesine on 2018/6/22.
 */

public class Menu extends AppCompatActivity {

    private CardView cardView0;
    private CardView cardView1;
    private CardView cardView2;
    private CardView cardView3;
    private CardView cv_result;
    private ImageView iv_barcode_result;
    private Map<String, Object> resultMap;

    private boolean flag0 = false;
    private boolean flag1 = false;
    private boolean flag2 = false;
    private boolean flag3 = false;

    private static final Class<?>[] ACTIVITY = {AssemblyTestActivity.class, FactoryAutoTest.class, PerformanceTestActivity.class,};
    private static final int[] REQUEST_CODE = {1000, 1001, 1002};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.hideBottomUIMenu(this);
        setContentView(R.layout.act_menu);

        initView();

        initListener();

        initData();

    }

    private void initListener() {
        cardView0.setOnClickListener(cardView0Listener);
        cardView1.setOnClickListener(cardView1Listener);
        cardView2.setOnClickListener(cardView2Listener);
        cardView3.setOnClickListener(cardView3Listener);
    }

    private void initData() {
        resultMap = new HashMap<>();
        resultMap.put(Constans.SERIAL, Build.SERIAL);
        resultMap.put(Constans.ETH0, DeviceUtil.getEth0());
        resultMap.put(Constans.WLAN, DeviceUtil.getWifiMac(this));
    }

    private void initView() {
        cardView0 = (CardView) findViewById(R.id.cardView0);
        cardView1 = (CardView) findViewById(R.id.cardView1);
        cardView2 = (CardView) findViewById(R.id.cardView2);
        cardView3 = (CardView) findViewById(R.id.cardView3);
        cv_result = (CardView) findViewById(R.id.cv_result);
        iv_barcode_result = (ImageView) findViewById(R.id.iv_barcode_result);
        refreshUI(TestMode.ASSEMBLY);
    }

    View.OnClickListener cardView0Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!flag0) {
                return;
            }
            startAct(0);
        }
    };
    View.OnClickListener cardView1Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!flag1) {
                return;
            }
            startAct(1);
        }
    };
    View.OnClickListener cardView2Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!flag2) {
                return;
            }
            startAct(2);
        }
    };
    View.OnClickListener cardView3Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!flag3) {
                return;
            }
            //TODO 清空数据
            SharedPreferencesUtils.clearSpfs(Menu.this);
            App.exit();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        resultMap.clear();
    }

    private void startAct(int position) {
        Intent intent = new Intent(Menu.this, ACTIVITY[position]);
        startActivityForResult(intent, REQUEST_CODE[position]);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE[0]) {
//                Assembly assembly = (Assembly) data.getSerializableExtra(Constans.TEST_ASSEMBLY);
//                SharedPreferencesUtils.putToSpfs(this, Constans.TEST_ASSEMBLY, assembly);
                refreshUI(TestMode.AGEING);
            } else if (requestCode == REQUEST_CODE[1]) {
//                Ageing ageing = (Ageing) data.getSerializableExtra(Constans.TEST_AGING);
//                SharedPreferencesUtils.putToSpfs(this, Constans.TEST_AGING, ageing);
                refreshUI(TestMode.PERFORMANCE);
            } else if (requestCode == REQUEST_CODE[2]) {
//                Performance performance = (Performance) data.getSerializableExtra(Constans.TEST_PERFORMANCE);
//                SharedPreferencesUtils.putToSpfs(this, Constans.TEST_PERFORMANCE, performance);
                showBarcode();
                refreshUI(TestMode.CLEAN);
            }
        }
    }

    private void showBarcode() {
//        Assembly assembly = (Assembly) SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_ASSEMBLY, null);
//        Ageing ageing = (Ageing) SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_AGING, null);
//        Performance performance = (Performance) SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_PERFORMANCE, null);
//        resultMap.put(Constans.TEST_ASSEMBLY_0, assembly.getVisualInspection());
//        resultMap.put(Constans.TEST_ASSEMBLY_1, assembly.getCamera());
//        resultMap.put(Constans.TEST_ASSEMBLY_2, assembly.getScanner());
//        resultMap.put(Constans.TEST_ASSEMBLY_3, assembly.getTouch());
//        resultMap.put(Constans.TEST_ASSEMBLY_4, assembly.getAutoTouch());
//        resultMap.put(Constans.TEST_ASSEMBLY_5, assembly.getAudio());
//        resultMap.put(Constans.TEST_ASSEMBLY_6, assembly.getEthernet());
//        resultMap.put(Constans.TEST_AGING_0, ageing.getAging());
//        resultMap.put(Constans.TEST_PERFORMANCE_0, performance.getDisplayVersion());
//        resultMap.put(Constans.TEST_PERFORMANCE_1, performance.getSerialNo());
//        resultMap.put(Constans.TEST_PERFORMANCE_2, performance.getEth0());
//        resultMap.put(Constans.TEST_PERFORMANCE_3, performance.getVisualInspection());
//        resultMap.put(Constans.TEST_PERFORMANCE_4, performance.getCamera());
//        resultMap.put(Constans.TEST_PERFORMANCE_5, performance.getScanner());
//        resultMap.put(Constans.TEST_PERFORMANCE_6, performance.getTouch());
//        resultMap.put(Constans.TEST_PERFORMANCE_7, performance.getAutoTouch());
//        resultMap.put(Constans.TEST_PERFORMANCE_8, performance.getAudio());
//        resultMap.put(Constans.TEST_PERFORMANCE_9, performance.getAudio());
//        resultMap.put(Constans.TEST_PERFORMANCE_10, performance.getWifi());
        resultMap.put(Constans.TEST_ASSEMBLY_0, SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_ASSEMBLY_0, 0) == null ? 0 : 1);
        resultMap.put(Constans.TEST_ASSEMBLY_1, SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_ASSEMBLY_1, 0) == null ? 0 : 1);
        resultMap.put(Constans.TEST_ASSEMBLY_2, SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_ASSEMBLY_2, 0) == null ? 0 : 1);
        resultMap.put(Constans.TEST_ASSEMBLY_3, SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_ASSEMBLY_3, 0) == null ? 0 : 1);
        resultMap.put(Constans.TEST_ASSEMBLY_4, SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_ASSEMBLY_4, 0) == null ? 0 : 1);
        resultMap.put(Constans.TEST_ASSEMBLY_5, SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_ASSEMBLY_5, 0) == null ? 0 : 1);
        resultMap.put(Constans.TEST_ASSEMBLY_6, SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_ASSEMBLY_6, 0) == null ? 0 : 1);
        resultMap.put(Constans.TEST_AGING_0, SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_AGING_0, 0) == null ? 0 : 1);
        resultMap.put(Constans.TEST_PERFORMANCE_0, SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_PERFORMANCE_0, 0) == null ? 0 : 1);
        resultMap.put(Constans.TEST_PERFORMANCE_1, SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_PERFORMANCE_1, 0) == null ? 0 : 1);
        resultMap.put(Constans.TEST_PERFORMANCE_2, SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_PERFORMANCE_2, 0) == null ? 0 : 1);
        resultMap.put(Constans.TEST_PERFORMANCE_3, SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_PERFORMANCE_3, 0) == null ? 0 : 1);
        resultMap.put(Constans.TEST_PERFORMANCE_4, SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_PERFORMANCE_4, 0) == null ? 0 : 1);
        resultMap.put(Constans.TEST_PERFORMANCE_5, SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_PERFORMANCE_5, 0) == null ? 0 : 1);
        resultMap.put(Constans.TEST_PERFORMANCE_6, SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_PERFORMANCE_6, 0) == null ? 0 : 1);
        resultMap.put(Constans.TEST_PERFORMANCE_7, SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_PERFORMANCE_7, 0) == null ? 0 : 1);
        resultMap.put(Constans.TEST_PERFORMANCE_8, SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_PERFORMANCE_8, 0) == null ? 0 : 1);
        resultMap.put(Constans.TEST_PERFORMANCE_9, SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_PERFORMANCE_9, 0) == null ? 0 : 1);
        resultMap.put(Constans.TEST_PERFORMANCE_10, SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_PERFORMANCE_10, 0) == null ? 0 : 1);
        showBarcode(resultMap.toString());
    }


    private void showBarcode(String code) {
        Bitmap wesine = GenerateUtil.generateBitmap(code, 500, 500);
        iv_barcode_result.setImageBitmap(wesine);
        if (cv_result.getVisibility() == View.VISIBLE) {
            return;
        }
        cv_result.setVisibility(View.VISIBLE);
    }

    private void refreshUI(TestMode mode) {
        switch (mode) {
            case ASSEMBLY:
                flag0 = true;
                flag1 = false;
                flag2 = false;
                flag3 = false;
                break;
            case AGEING:
                flag0 = false;
                flag1 = true;
                flag2 = false;
                flag3 = false;
                break;

            case PERFORMANCE:
                flag0 = false;
                flag1 = false;
                flag2 = true;
                flag3 = false;
                break;

            case CLEAN:
                flag0 = false;
                flag1 = false;
                flag2 = false;
                flag3 = true;
                break;
        }
    }

    enum TestMode {
        ASSEMBLY, AGEING, PERFORMANCE, CLEAN
    }
}
