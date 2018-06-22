package org.doug.monitor.assembly;

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
import org.doug.monitor.base.networktools.NetworktoolsTestActivity;
import org.doug.monitor.base.util.Toaster;
import org.doug.monitor.scanner.ToolScannerTestActivity;
import org.doug.monitor.camera.CameraTestActivity;
import org.doug.monitor.menu.MenuAdapter;
import org.doug.monitor.menu.MenuItem;
import org.doug.monitor.touch.TouchActivity;
import org.doug.monitor.visualInspection.VisualInspectionActivity;

import java.util.ArrayList;

/**
 * Created by wesine on 2018/6/14.
 */

public class AssemblyTestActivity extends BaseActivity {

    private static final int[] ASSEMBLY_REQUEST_CODE = {2000, 2001, 2002, 2003, 2004, 2005, 2006};
    private ArrayList<MenuItem> items;

    private String[] titles = {"外观检测", "摄像头", "扫码器",
            "触摸手动测试", "触摸自动测试", "喇叭测试",
            "有线网络测试"};

    private static final Class<?>[] ASSEMBLYACTIVITYS = {VisualInspectionActivity.class, CameraTestActivity.class, ToolScannerTestActivity.class,
            TouchActivity.class, TouchActivity.class, AudioTestActivity.class,
            NetworktoolsTestActivity.class};

    private static final int[] IMGS = {R.drawable.ic_visualinspection, R.drawable.ic_camera, R.drawable.ic_scanner,
            R.drawable.ic_touch, R.drawable.ic_touch, R.drawable.ic_audio,
            R.drawable.ic_ethernet};

    private Assembly assembly;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assembly);
        setTitle("组装检测");
//        setBackBtn();
        assembly = new Assembly();
        RecyclerView rv_assembly = (RecyclerView) findViewById(R.id.rv_assembly);
        initData();
        BaseQuickAdapter adapter = new MenuAdapter(R.layout.menu_item_view, items);
        adapter.openLoadAnimation();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(AssemblyTestActivity.this, ASSEMBLYACTIVITYS[position]);
                 startActivityForResult(intent, ASSEMBLY_REQUEST_CODE[position]);
            }
        });
        rv_assembly.setLayoutManager(new LinearLayoutManager(this));
        rv_assembly.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        rv_assembly.addItemDecoration(dividerItemDecoration);
        rv_assembly.setAdapter(adapter);
    }

    private void initData() {
        items = new ArrayList<MenuItem>();
        for (int i = 0; i < titles.length; i++) {
            MenuItem item = new MenuItem();
            item.setTitle(titles[i]);
            item.setActivity(ASSEMBLYACTIVITYS[i]);
            item.setImageResource(IMGS[i]);
            items.add(item);
        }
    }

    @Override
    protected void onDestroy() {
        assembly = null;
        items.clear();
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ASSEMBLY_REQUEST_CODE[0]) {
                Toaster.showToast(this, titles[0]);
                assembly.setVisualInspection(1);
            } else if (requestCode == ASSEMBLY_REQUEST_CODE[1]) {
                Toaster.showToast(this, titles[1]);
                assembly.setCamera(1);
            } else if (requestCode == ASSEMBLY_REQUEST_CODE[2]) {
                Toaster.showToast(this, titles[2]);
                assembly.setScanner(1);
            } else if (requestCode == ASSEMBLY_REQUEST_CODE[3]) {
                Toaster.showToast(this, titles[3]);
                assembly.setTouch(1);
            } else if (requestCode == ASSEMBLY_REQUEST_CODE[4]) {
                Toaster.showToast(this, titles[4]);
                assembly.setAutoTouch(1);
            } else if (requestCode == ASSEMBLY_REQUEST_CODE[5]) {
                Toaster.showToast(this, titles[5]);
                assembly.setAudio(1);
            } else if (requestCode == ASSEMBLY_REQUEST_CODE[6]) {
                Toaster.showToast(this, titles[6]);
                assembly.setEthernet(1);
                Intent intent = new Intent();
                intent.putExtra(Constans.TEST_ASSEMBLY, assembly);
                setResult(RESULT_OK, intent);
                this.finish();
            } else {
                Toaster.showToast(this, "操作有误！");
            }
        }
    }
}

