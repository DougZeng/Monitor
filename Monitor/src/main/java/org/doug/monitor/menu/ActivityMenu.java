package org.doug.monitor.menu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;

import org.doug.monitor.assembly.Assembly;
import org.doug.monitor.assembly.AssemblyTestActivity;
import org.doug.monitor.clean.CleanDataActivity;
import org.doug.monitor.performance.Performance;
import org.doug.monitor.performance.PerformanceTestActivity;
import org.doug.monitor.ageing.Ageing;
import org.doug.monitor.R;
import org.doug.monitor.base.Constans;
import org.doug.monitor.base.util.GenerateUtil;
import org.doug.monitor.base.util.ScreenUtils;
import org.doug.monitor.factorytest.FactoryAutoTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wesine on 2018/5/23.
 */

public class ActivityMenu extends AppCompatActivity {
    private static final Class<?>[] ACTIVITY = {AssemblyTestActivity.class, FactoryAutoTest.class, PerformanceTestActivity.class, CleanDataActivity.class};
    private static final String[] TITLE = {"组装检测", "老化测试", "电性能测试", "清空数据"};
    private static final int[] IMG = {R.drawable.menu_assembly, R.drawable.menu_aging, R.drawable.menu_performance,R.drawable.ic_clean};
    private static final int[] REQUEST_CODE = {1000, 1001, 1002, 1003};

    private RecyclerView mRecyclerView;
    private List<MenuItem> mDataList;
    private ImageView iv_barcode_result;
    private HashMap<String, Object> mapResult;
    private CardView cardView;

    private static final String RESULTDATA = "resultData";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.hideBottomUIMenu(this);

//        int bootCount = (int) SharedPreferencesUtils.getFromSpfs(this, Constans.IS_FIRST_BOOT, Constans.DEFAULT_COUNT);
//        if (bootCount == 0) {
//            SharedPreferencesUtils.putToSpfs(this, Constans.IS_FIRST_BOOT, 1);
//            Intent intent = new Intent(this, FactoryAutoTest.class);
//            intent.setAction(Constans.actionAutoTest);
//            startActivity(intent);
//        }
        setContentView(R.layout.activity_menu);
        initView();
        initData();
        initAdapter();
    }

    private void initAdapter() {
        BaseQuickAdapter homeAdapter = new MenuAdapter(R.layout.menu_item_view, mDataList);
        homeAdapter.openLoadAnimation();
        View top = getLayoutInflater().inflate(R.layout.top_view, (ViewGroup) mRecyclerView.getParent(), false);
        homeAdapter.addHeaderView(top);
        homeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(ActivityMenu.this, ACTIVITY[position]);
                startActivityForResult(intent, REQUEST_CODE[position]);
            }
        });

        mRecyclerView.setAdapter(homeAdapter);
    }

    private void initData() {
        mDataList = new ArrayList<>();
        for (int i = 0; i < TITLE.length; i++) {
            MenuItem item = new MenuItem();
            item.setTitle(TITLE[i]);
            item.setActivity(ACTIVITY[i]);
            item.setImageResource(IMG[i]);
            mDataList.add(item);
        }

        mapResult = new HashMap<>();
        mapResult.put(Constans.SERIAL, Build.SERIAL);
    }

    private void initView() {
        cardView = (CardView) findViewById(R.id.cv_result);
        iv_barcode_result = (ImageView) findViewById(R.id.iv_barcode_result);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE[0]) {
                Assembly assembly = (Assembly) data.getSerializableExtra(Constans.TEST_ASSEMBLY);
                mapResult.put(Constans.TEST_ASSEMBLY_0, assembly.getVisualInspection());
                mapResult.put(Constans.TEST_ASSEMBLY_1, assembly.getCamera());
                mapResult.put(Constans.TEST_ASSEMBLY_2, assembly.getScanner());
                mapResult.put(Constans.TEST_ASSEMBLY_3, assembly.getTouch());
                mapResult.put(Constans.TEST_ASSEMBLY_4, assembly.getAutoTouch());
                mapResult.put(Constans.TEST_ASSEMBLY_5, assembly.getAudio());
                mapResult.put(Constans.TEST_ASSEMBLY_6, assembly.getEthernet());
            } else if (requestCode == REQUEST_CODE[1]) {
                Ageing ageing = (Ageing) data.getSerializableExtra(Constans.TEST_AGING);
                mapResult.put(Constans.TEST_AGING_0, ageing.getAging());
            } else if (requestCode == REQUEST_CODE[2]) {
                Performance performance = (Performance) data.getSerializableExtra(Constans.TEST_PERFORMANCE);
                mapResult.put(Constans.TEST_PERFORMANCE_0, performance.getDisplayVersion());
                mapResult.put(Constans.TEST_PERFORMANCE_1, performance.getSerialNo());
                mapResult.put(Constans.TEST_PERFORMANCE_2, performance.getEth0());
                mapResult.put(Constans.TEST_PERFORMANCE_3, performance.getVisualInspection());
                mapResult.put(Constans.TEST_PERFORMANCE_4, performance.getCamera());
                mapResult.put(Constans.TEST_PERFORMANCE_5, performance.getScanner());
                mapResult.put(Constans.TEST_PERFORMANCE_6, performance.getTouch());
                mapResult.put(Constans.TEST_PERFORMANCE_7, performance.getAutoTouch());
                mapResult.put(Constans.TEST_PERFORMANCE_8, performance.getAudio());
                mapResult.put(Constans.TEST_PERFORMANCE_9, performance.getAudio());
                mapResult.put(Constans.TEST_PERFORMANCE_10, performance.getWifi());
                showBarcode(mapResult.toString());
                Logger.d(mapResult.toString());
            }
        }
    }

    private void showBarcode(String code) {
        Bitmap wesine = GenerateUtil.generateBitmap(code, 500, 500);
        iv_barcode_result.setImageBitmap(wesine);
        cardView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(RESULTDATA, mapResult.toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String string = savedInstanceState.getString(RESULTDATA);
        showBarcode(string);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
