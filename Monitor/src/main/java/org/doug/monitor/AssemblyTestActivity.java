package org.doug.monitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.doug.monitor.base.util.Toaster;
import org.doug.monitor.menu.CommonAdapter;
import org.doug.monitor.menu.DeviceItem;

import java.util.ArrayList;

/**
 * Created by wesine on 2018/6/14.
 */

public class AssemblyTestActivity extends Activity implements BaseQuickAdapter.OnItemClickListener {

    private ArrayList<DeviceItem> items;

    String[] titles = {"外观检测", "摄像头", "扫码器", "触摸手动测试", "触摸自动测试", "喇叭测试", "有线网络测试"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assembly);
        RecyclerView rv_assembly = (RecyclerView) findViewById(R.id.rv_assembly);
        items = new ArrayList<>();
        items.add(new DeviceItem(titles[0], "", false));
        items.add(new DeviceItem(titles[1], "", false));
        items.add(new DeviceItem(titles[2], "", false));
        items.add(new DeviceItem(titles[3], "", false));
        items.add(new DeviceItem(titles[4], "", false));
        items.add(new DeviceItem(titles[5], "", false));
        items.add(new DeviceItem(titles[6], "15秒，丢包率限定", false));
        CommonAdapter adapter = new CommonAdapter(items);
        adapter.setOnItemClickListener(this);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        rv_assembly.setLayoutManager(new LinearLayoutManager(this));
        rv_assembly.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        rv_assembly.addItemDecoration(dividerItemDecoration);
        rv_assembly.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        items.clear();
        super.onDestroy();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//        Intent intent = getIntent();
        if (position == 0) {
            Toaster.showToast(this, titles[0]);
        } else if (position == 1) {
            Toaster.showToast(this, titles[1]);
        } else if (position == 2) {
            Toaster.showToast(this, titles[2]);
        } else if (position == 3) {
            Toaster.showToast(this, titles[3]);
        } else if (position == 4) {
            Toaster.showToast(this, titles[4]);
        } else if (position == 5) {
            Toaster.showToast(this, titles[5]);
        } else if (position == 6) {
            Toaster.showToast(this, titles[6]);
        } else {
            return;
        }
//        startActivity(intent);
    }
}

