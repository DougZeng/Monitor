package org.doug.monitor.menu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.doug.monitor.R;
import org.doug.monitor.assembly.AssemblyTestActivity;
import org.doug.monitor.base.App;
import org.doug.monitor.base.Constans;
import org.doug.monitor.base.util.DeviceUtil;
import org.doug.monitor.base.util.GenerateUtil;
import org.doug.monitor.base.util.ScreenUtils;
import org.doug.monitor.base.util.SharedPreferencesUtils;
import org.doug.monitor.base.util.Toaster;
import org.doug.monitor.factorytest.FactoryAutoTest;
import org.doug.monitor.performance.PerformanceTestActivity;

import java.io.File;
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
    private TextView tv_show_result;

    private String result = Constans.PASS;

    private boolean flag0 = false;
    private boolean flag1 = false;
    private boolean flag2 = false;
    private boolean flag3 = false;

    private static final Class<?>[] ACTIVITY = {AssemblyTestActivity.class, FactoryAutoTest.class, PerformanceTestActivity.class,};
    private static final int[] REQUEST_CODE = {1000, 1001, 1002};

    private static final int MSG_1 = 1000;

    private int CLEANCLICKED = 0;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_1:
                    App.getApp().exit();
                    break;
            }
            return true;
        }
    });
    private ImageButton fab_clean;

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
    }

    private void initView() {
        fab_clean = (ImageButton) findViewById(R.id.fab_clean);
        fab_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CLEANCLICKED++;
                if (CLEANCLICKED > 1) {
                    Toaster.showToast(Menu.this, "请勿重复点击!");
                    return;
                }
                cleanData();
            }
        });
        cardView0 = (CardView) findViewById(R.id.cardView0);
        cardView1 = (CardView) findViewById(R.id.cardView1);
        cardView2 = (CardView) findViewById(R.id.cardView2);
        cardView3 = (CardView) findViewById(R.id.cardView3);
        cv_result = (CardView) findViewById(R.id.cv_result);
        iv_barcode_result = (ImageView) findViewById(R.id.iv_barcode_result);
        tv_show_result = (TextView) findViewById(R.id.tv_show_result);
        refreshUI(TestMode.ASSEMBLY);
    }

    View.OnClickListener cardView0Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startAct(0);
        }
    };
    View.OnClickListener cardView1Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startAct(1);
        }
    };
    View.OnClickListener cardView2Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startAct(2);
        }
    };
    View.OnClickListener cardView3Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cleanData();

        }
    };

    private void cleanData() {
        //TODO 清空数据
        SharedPreferencesUtils.clearSpfs(Menu.this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                    File file = new File(path, Environment.DIRECTORY_PICTURES);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    App.getApp().deleteFile(file);
                    handler.sendEmptyMessageDelayed(MSG_1, 3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                refreshUI(TestMode.AGEING);
            } else if (requestCode == REQUEST_CODE[1]) {
                refreshUI(TestMode.PERFORMANCE);
            } else if (requestCode == REQUEST_CODE[2]) {
                getTestData();
                refreshUI(TestMode.CLEAN);
            }
        }
    }

    private void getTestData() {
        String assembly_0 = (String) SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_ASSEMBLY_0, Constans.DEFAULT_NA);
        String assembly_1 = (String) SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_ASSEMBLY_1, Constans.DEFAULT_NA);
        String assembly_2 = (String) SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_ASSEMBLY_2, Constans.DEFAULT_NA);
        String assembly_3 = (String) SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_ASSEMBLY_3, Constans.DEFAULT_NA);
        String assembly_4 = (String) SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_ASSEMBLY_4, Constans.DEFAULT_NA);
        String assembly_5 = (String) SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_ASSEMBLY_5, Constans.DEFAULT_NA);
        String assembly_6 = (String) SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_ASSEMBLY_6, Constans.DEFAULT_NA);
        String aging_0 = (String) SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_AGING_0, Constans.DEFAULT_NA);
        String performance_0 = (String) SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_PERFORMANCE_0, Constans.DEFAULT_NA);
        String performance_1 = (String) SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_PERFORMANCE_1, Constans.DEFAULT_NA);
        String performance_2 = (String) SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_PERFORMANCE_2, Constans.DEFAULT_NA);
        String performance_3 = (String) SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_PERFORMANCE_3, Constans.DEFAULT_NA);
        String performance_4 = (String) SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_PERFORMANCE_4, Constans.DEFAULT_NA);
        String performance_5 = (String) SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_PERFORMANCE_5, Constans.DEFAULT_NA);
        String performance_6 = (String) SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_PERFORMANCE_6, Constans.DEFAULT_NA);
        String performance_7 = (String) SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_PERFORMANCE_7, Constans.DEFAULT_NA);
        String performance_8 = (String) SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_PERFORMANCE_8, Constans.DEFAULT_NA);
        String performance_9 = (String) SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_PERFORMANCE_9, Constans.DEFAULT_NA);
        String performance_10 = (String) SharedPreferencesUtils.getFromSpfs(this, Constans.TEST_PERFORMANCE_10, Constans.DEFAULT_NA);
        StringBuilder builder = new StringBuilder();
        builder.append(assembly_0);
        builder.append("\t");
        builder.append(assembly_1);
        builder.append("\t");
        builder.append(assembly_2);
        builder.append("\t");
        builder.append(assembly_3);
        builder.append("\t");
        builder.append(assembly_4);
        builder.append("\t");
        builder.append(assembly_5);
        builder.append("\t");
        builder.append(assembly_6);
        builder.append("\t");
        builder.append(aging_0);
        builder.append("\t");
        builder.append(performance_0);
        builder.append("\t");
        builder.append(performance_1);
        builder.append("\t");
        builder.append(performance_2);
        builder.append("\t");
        builder.append(performance_3);
        builder.append("\t");
        builder.append(performance_4);
        builder.append("\t");
        builder.append(performance_5);
        builder.append("\t");
        builder.append(performance_6);
        builder.append("\t");
        builder.append(performance_7);
        builder.append("\t");
        builder.append(performance_8);
        builder.append("\t");
        builder.append(performance_9);
        builder.append("\t");
        builder.append(performance_10);
        builder.append("\n");
        String string = builder.toString();
        Logger.d(string);
        showBarcode(string);
    }


    private void showBarcode(String code) {
        Bitmap wesine = GenerateUtil.generateBitmap(code, 250, 250);
        iv_barcode_result.setImageBitmap(wesine);
        if (cv_result.getVisibility() == View.VISIBLE) {
            return;
        }
        cv_result.setVisibility(View.VISIBLE);
        String[] split = code.replace("\n", "").split("\t");
        for (int i = 0; i < split.length; i++) {
            if (split[i].equals(Constans.FAIL) || split[i].equals(Constans.DEFAULT_NA)) {
                result = Constans.FAIL;
                break;
            }
        }
        tv_show_result.setText(result);
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
