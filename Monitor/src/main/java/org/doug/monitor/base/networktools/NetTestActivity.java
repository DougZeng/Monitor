package org.doug.monitor.base.networktools;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.doug.monitor.R;
import org.doug.monitor.base.BaseActivity;
import org.doug.monitor.base.Constans;
import org.doug.monitor.base.circleprogressbar.CountDownView;
import org.doug.monitor.base.networktools.ping.PingResult;
import org.doug.monitor.base.networktools.ping.PingStats;
import org.doug.monitor.base.networktools.subnet.Device;
import org.doug.monitor.base.util.DeviceUtil;
import org.doug.monitor.base.util.SharedPreferencesUtils;
import org.doug.monitor.base.util.Toaster;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by wesine on 2018/6/14.
 */

public class NetTestActivity extends BaseActivity implements CountDownView.OnTimeCompleteListener {

    private TextView resultText;
    private EditText editIpAddress;
    private Button pingButton;
    private CountDownView cdv_second;
    private static final int MSG_2 = 1101;
    private static final int MSG_22 = 2202;
    private static final int MSG_33 = 3303;


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg != null) {
                if (msg.what == MSG_2) {
                    cdv_second.start();
                    Toaster.showToast(NetTestActivity.this, "测试倒计时" + Constans.TEST_TIME_NETWORK + "秒！");
                    pingButton.performClick();
                } else if (msg.what == MSG_22) {
                    long packetsLost = (long) msg.obj;
                    tv_pack_lost.setText(String.valueOf(packetsLost));
                } else if (msg.what == MSG_33) {
                    if (networkMode.equals(Constans.ACTION_A + 5)) {
                        SharedPreferencesUtils.putToSpfs(NetTestActivity.this, Constans.TEST_ASSEMBLY_5, Constans.PASS);
                    } else if (networkMode.equals(Constans.ACTION_A + 6)) {
                        SharedPreferencesUtils.putToSpfs(NetTestActivity.this, Constans.TEST_ASSEMBLY_6, Constans.PASS);
                    } else if (networkMode.equals(Constans.ACTION_P + 6)) {
                        SharedPreferencesUtils.putToSpfs(NetTestActivity.this, Constans.TEST_PERFORMANCE_9, Constans.PASS);
                    } else if (networkMode.equals(Constans.ACTION_P + 7)) {
                        SharedPreferencesUtils.putToSpfs(NetTestActivity.this, Constans.TEST_PERFORMANCE_10, Constans.PASS);
                    }
                    setResult(RESULT_OK);
                    NetTestActivity.this.finish();
                }
            }
            return true;
        }
    });
    private TextView tv_avg_time;
    private TextView tv_min_time;
    private TextView tv_pack_lost;
    private TextView tv_max_time;
    private TextView tv_network_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_networktoolstest);
        setTitle("网络测试");
        setBackBtn();
        initView();
        initListener();
        initData();

    }

    private String networkMode = "";

    private void initData() {

    }


    private void initListener() {
        cdv_second.initTime(Constans.TEST_TIME_NETWORK);
        cdv_second.setOnTimeCompleteListener(this);

//        InetAddress ipAddress = IPTools.getLocalIPv4Address();
//        if (ipAddress != null) {
//            editIpAddress.setText(ipAddress.getHostAddress());
//        }
        editIpAddress.setText("baidu.com");
        pingButton = (Button) findViewById(R.id.pingButton);
        pingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            doPing();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        findViewById(R.id.wolButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            doWakeOnLan();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        findViewById(R.id.portScanButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            doPortScan();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        findViewById(R.id.subnetDevicesButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            findSubnetDevices();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    private void initView() {
        tv_network_result = (TextView) findViewById(R.id.tv_network_result);
        tv_pack_lost = (TextView) findViewById(R.id.tv_pack_lost);
        tv_min_time = (TextView) findViewById(R.id.tv_min_time);
        tv_avg_time = (TextView) findViewById(R.id.tv_avg_time);
        tv_max_time = (TextView) findViewById(R.id.tv_max_time);
        resultText = (TextView) findViewById(R.id.resultText);
        editIpAddress = (EditText) findViewById(R.id.editIpAddress);

        cdv_second = (CountDownView) findViewById(R.id.countdown_timer_second);
    }

    private void appendResultsText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultText.append(text + "\n");
            }
        });
    }

    private void doPing() throws Exception {
        String ipAddress = editIpAddress.getText().toString();

        if (TextUtils.isEmpty(ipAddress)) {
            appendResultsText("Invalid Ip Address");
            return;
        }

        // Perform a single synchronous ping
        PingResult pingResult = Ping.onAddress(ipAddress).setTimeOutMillis(1000).doPing();

        appendResultsText("Pinging Address: " + pingResult.getAddress().getHostAddress());
        appendResultsText("HostName: " + pingResult.getAddress().getHostName());
        appendResultsText(String.format("%.2f ms", pingResult.getTimeTaken()));


        // Perform an asynchronous ping
        Ping.onAddress(ipAddress).setTimeOutMillis(1000).setTimes(100).doPing(new Ping.PingListener() {
            @Override
            public void onResult(PingResult pingResult) {
                appendResultsText(String.format("%.2f ms", pingResult.getTimeTaken()));
            }

            @Override
            public void onFinished(PingStats pingStats) {
                appendResultsText(String.format("Pings: %d, Packets lost: %d",
                        pingStats.getNoPings(), pingStats.getPacketsLost()));
                appendResultsText(String.format("Min/Avg/Max Time: %.2f/%.2f/%.2f ms",
                        pingStats.getMinTimeTaken(), pingStats.getAverageTimeTaken(), pingStats.getMaxTimeTaken()));
//                tv_pack_lost.setText(String.valueOf(pingStats.getPacketsLost()));
//                tv_min_time.setText(String.valueOf(pingStats.getMinTimeTaken()));
//                tv_avg_time.setText(String.valueOf(pingStats.getAverageTimeTaken()));
//                tv_max_time.setText(String.valueOf(pingStats.getMaxTimeTaken()));
                Message message = new Message();
                message.what = MSG_22;
                message.obj = pingStats.getPacketsLost();
                handler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

    private void doWakeOnLan() throws IllegalArgumentException {
        String ipAddress = editIpAddress.getText().toString();

        if (TextUtils.isEmpty(ipAddress)) {
            appendResultsText("Invalid Ip Address");
            return;
        }

        appendResultsText("IP address: " + ipAddress);

        // Get mac address from IP (using arp cache)
        String macAddress = ARPInfo.getMACFromIPAddress(ipAddress);

        if (macAddress == null) {
            appendResultsText("Could not fromIPAddress MAC address, cannot send WOL packet without it.");
            return;
        }

        appendResultsText("MAC address: " + macAddress);
        appendResultsText("IP address2: " + ARPInfo.getIPAddressFromMAC(macAddress));

        // Send Wake on lan packed to ip/mac
        try {
            WakeOnLan.sendWakeOnLan(ipAddress, macAddress);
            appendResultsText("WOL Packet sent");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doPortScan() throws Exception {
        String ipAddress = editIpAddress.getText().toString();

        if (TextUtils.isEmpty(ipAddress)) {
            appendResultsText("Invalid Ip Address");
            return;
        }

        // Perform synchronous port scan
        appendResultsText("PortScanning IP: " + ipAddress);
        ArrayList<Integer> openPorts = PortScan.onAddress(ipAddress).setPort(21).doScan();

        final long startTimeMillis = System.currentTimeMillis();

        // Perform an asynchronous port scan
        PortScan.onAddress(ipAddress).setPortsAll().doScan(new PortScan.PortListener() {
            @Override
            public void onResult(int portNo, boolean open) {
                if (open) appendResultsText("Open: " + portNo);
            }

            @Override
            public void onFinished(ArrayList<Integer> openPorts) {
                appendResultsText("Open Ports: " + openPorts.size());
                appendResultsText("Time Taken: " + ((System.currentTimeMillis() - startTimeMillis) / 1000.0f));
            }
        });

    }


    private void findSubnetDevices() {

        final long startTimeMillis = System.currentTimeMillis();

        SubnetDevices.fromLocalAddress().findDevices(new SubnetDevices.OnSubnetDeviceFound() {
            @Override
            public void onDeviceFound(Device device) {
                appendResultsText("Device: " + device.ip + " " + device.hostname);
            }

            @Override
            public void onFinished(ArrayList<Device> devicesFound) {
                float timeTaken = (System.currentTimeMillis() - startTimeMillis) / 1000.0f;
                appendResultsText("Devices Found: " + devicesFound.size());
                appendResultsText("Finished " + timeTaken + " s");
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        cdv_second.onPause();
    }

    public static final String[] NET_TYPE_NAME = {"Ethernet", "WIFI"};

    private String netTypeName = "";

    @Override
    protected void onResume() {
        super.onResume();
        ConnectivityManager mConnectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            if (!mNetworkInfo.isAvailable()) {
                Toaster.showToast(this, "没有网络了！");
                return;
            }
            String typeName = mNetworkInfo.getTypeName();
            netTypeName = typeName;
            Logger.d(typeName);
            Logger.d(mNetworkInfo.toString());
        }


        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action)) {
                networkMode = action;
                if (action.equals(Constans.ACTION_A + 5)) {
                    if (!netTypeName.equals(NET_TYPE_NAME[0])) {
                        DeviceUtil.execLinuxCommand(DeviceUtil.CMD_SETTINGS);
                    } else {
                        handler.sendEmptyMessageDelayed(MSG_2, Constans.DELAYMILLIS);
                    }
                } else if (action.equals(Constans.ACTION_A + 6)) {
                    if (!netTypeName.equals(NET_TYPE_NAME[1])) {
                        DeviceUtil.execLinuxCommand(DeviceUtil.CMD_SETTINGS);
                    } else {
                        handler.sendEmptyMessageDelayed(MSG_2, Constans.DELAYMILLIS);
                    }
                } else if (action.equals(Constans.ACTION_P + 6)) {
                    if (!netTypeName.equals(NET_TYPE_NAME[0])) {
                        DeviceUtil.execLinuxCommand(DeviceUtil.CMD_SETTINGS);
                    } else {
                        handler.sendEmptyMessageDelayed(MSG_2, Constans.DELAYMILLIS);
                    }
                } else if (action.equals(Constans.ACTION_P + 7)) {
                    if (!netTypeName.equals(NET_TYPE_NAME[1])) {
                        DeviceUtil.execLinuxCommand(DeviceUtil.CMD_SETTINGS);
                    } else {
                        handler.sendEmptyMessageDelayed(MSG_2, Constans.DELAYMILLIS);
                    }
                }
            }
        }
    }


    @Override
    public void onTimeComplete() {
        String trim = tv_pack_lost.getText().toString().trim();
        if (Integer.valueOf(trim) < 10) {
            tv_network_result.setText("Pass");
            handler.sendEmptyMessageDelayed(MSG_33, Constans.DELAYMILLIS);
        } else {
            tv_network_result.setText("Fail");
        }
    }
}
