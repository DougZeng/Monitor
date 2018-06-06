package org.doug.monitor.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.doug.monitor.base.Constans;
import org.doug.monitor.base.banner.test.SampleActivity;
import org.doug.monitor.base.util.SharedPreferencesUtils;
import org.doug.monitor.device.ActivityDevice;
import org.doug.monitor.monitor.ActivityMonitor;
import org.doug.monitor.base.App;
import org.doug.monitor.R;
import org.doug.monitor.factorytest.FactoryAutoTest;
import org.doug.monitor.netspeed.NetActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wesine on 2018/5/23.
 */

public class ActivityMenu extends AppCompatActivity implements BaseQuickAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private List<DeviceItem> deviceItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        int bootCount = (int) SharedPreferencesUtils.getFromSpfs(this, Constans.IS_FIRST_BOOT, Constans.DEFAULT_COUNT);
        if (bootCount == 0) {
            SharedPreferencesUtils.putToSpfs(this, Constans.IS_FIRST_BOOT, 1);
            Intent intent = new Intent(this, FactoryAutoTest.class);
            intent.setAction(Constans.actionAutoTest);
            startActivity(intent);
        }
        setContentView(R.layout.activity_menu);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        deviceItems = new ArrayList<DeviceItem>();
        deviceItems.add(new DeviceItem("设备", "设备配置", false));
        deviceItems.add(new DeviceItem("监控", "CPU memory 运行状态", false));
        deviceItems.add(new DeviceItem("网络", "带宽上下行测试", false));
//        deviceItems.add(new DeviceItem("定时开关机", "众云", false));
        deviceItems.add(new DeviceItem("老化测试", "24H", false));
//        deviceItems.add(new DeviceItem("banner", "banner", false));
        CommonAdapter adapter = new CommonAdapter(deviceItems);

        adapter.setOnItemClickListener(this);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent();
        if (position == 0) {
            intent.setClass(this, ActivityDevice.class);
        } else if (position == 1) {
            intent.setClass(this, ActivityMonitor.class);
        } else if (position == 2) {
            intent.setClass(this, NetActivity.class);
        } else if (position == 3) {
//            intent.setClass(this, ActivityTimerSwitch.class);
            intent.setClass(this, FactoryAutoTest.class);
        } else if (position == 4) {
//            intent.setClass(this, SampleActivity.class);
        }
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        deviceItems.clear();
        App.exit();
    }
}
