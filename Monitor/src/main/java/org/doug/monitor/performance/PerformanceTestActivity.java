package org.doug.monitor.performance;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.doug.monitor.R;
import org.doug.monitor.audio.AudioTestActivity;
import org.doug.monitor.base.BaseActivity;
import org.doug.monitor.base.Constans;
import org.doug.monitor.base.networktools.NetTestActivity;
import org.doug.monitor.base.util.Toaster;
import org.doug.monitor.scanner.ToolScannerTestActivity;
import org.doug.monitor.camera.CameraTestActivity;
import org.doug.monitor.version.VersionActivity;
import org.doug.monitor.menu.MenuAdapter;
import org.doug.monitor.menu.MenuItem;
import org.doug.monitor.touch.TouchActivity;
import org.doug.monitor.visualInspection.VisualInspectionActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wesine on 2018/6/14.
 */

public class PerformanceTestActivity extends BaseActivity {

    private static final Class<?>[] PERFORMANCEACTIVITYS = {VersionActivity.class, VisualInspectionActivity.class, CameraTestActivity.class,
            ToolScannerTestActivity.class, TouchActivity.class, AudioTestActivity.class,
            NetTestActivity.class, NetTestActivity.class};
    private static final int[] IMGS = {R.drawable.ic_version, R.drawable.ic_visualinspection, R.drawable.ic_camera,
            R.drawable.ic_scanner, R.drawable.ic_touch, R.drawable.ic_audio,
            R.drawable.ic_ethernet, R.drawable.ic_wifi};
    private List<MenuItem> items;
    //
    String[] titles = {"基础信息", "外观检测", "摄像头",
            "扫码器", "触摸测试", "喇叭测试",
            "有线网络测试", "无线网络测试"};

    Performance performance;
    private int[] PERFORMANCE_REQUEST_CODE = {3000, 3001, 3002, 3003, 3004, 3005, 3006, 3007};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);
        setTitle("电性能测试");
        setBackBtn();
        RecyclerView rv_performance = (RecyclerView) findViewById(R.id.rv_performance);
        initData();
        BaseQuickAdapter adapter = new MenuAdapter(R.layout.menu_item_view, items);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(PerformanceTestActivity.this, PERFORMANCEACTIVITYS[position]);
                intent.setAction(Constans.ACTION_P + position);
                startActivityForResult(intent, PERFORMANCE_REQUEST_CODE[position]);
            }
        });
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        rv_performance.setLayoutManager(new LinearLayoutManager(this));
        rv_performance.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        rv_performance.addItemDecoration(dividerItemDecoration);
        rv_performance.setAdapter(adapter);
    }

    private void initData() {
        performance = new Performance();
        items = new ArrayList<MenuItem>();
        for (int i = 0; i < titles.length; i++) {
            MenuItem menuItem = new MenuItem();
            menuItem.setTitle(titles[i]);
            menuItem.setActivity(PERFORMANCEACTIVITYS[i]);
            menuItem.setImageResource(IMGS[i]);
            items.add(menuItem);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PERFORMANCE_REQUEST_CODE[0]) {
                Toaster.showToast(this, titles[0]);
                performance.setDisplayVersion(1);
                performance.setSerialNo(1);
                performance.setE_mac(1);
                performance.setW_mac(1);
            } else if (requestCode == PERFORMANCE_REQUEST_CODE[1]) {
                Toaster.showToast(this, titles[1]);
                performance.setVisualInspection(1);
            } else if (requestCode == PERFORMANCE_REQUEST_CODE[2]) {
                Toaster.showToast(this, titles[3]);
                performance.setCamera(1);
            } else if (requestCode == PERFORMANCE_REQUEST_CODE[3]) {
                Toaster.showToast(this, titles[3]);
                performance.setScanner(1);
            } else if (requestCode == PERFORMANCE_REQUEST_CODE[4]) {
                Toaster.showToast(this, titles[4]);
                performance.setTouch(1);
            } else if (requestCode == PERFORMANCE_REQUEST_CODE[5]) {
                Toaster.showToast(this, titles[5]);
                performance.setAudio(1);
            } else if (requestCode == PERFORMANCE_REQUEST_CODE[6]) {
                Toaster.showToast(this, titles[6]);
                performance.setEthernet(1);
            } else if (requestCode == PERFORMANCE_REQUEST_CODE[7]) {
                Toaster.showToast(this, titles[7]);
                performance.setWifi(1);
                Intent intent = new Intent();
                intent.putExtra(Constans.TEST_PERFORMANCE, performance);
                setResult(RESULT_OK, intent);
                this.finish();
            } else {
                Toaster.showToast(this, "操作有误！");
            }
        }
    }


    @Override
    protected void onDestroy() {
        items.clear();
        super.onDestroy();
    }
}
