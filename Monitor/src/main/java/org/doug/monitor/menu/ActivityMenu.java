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
        setContentView(R.layout.activity_menu);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        deviceItems = new ArrayList<DeviceItem>();
        deviceItems.add(new DeviceItem("设备", "设备配置", false));
        deviceItems.add(new DeviceItem("监控", "CPU memory 运行状态", false));
        deviceItems.add(new DeviceItem("测试", "测试配置", false));
//        deviceItems.add(new DeviceItem("定时开关机", "众云", false));
        deviceItems.add(new DeviceItem("老化测试", "24H", false));
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
        }
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.exit();
    }
}
