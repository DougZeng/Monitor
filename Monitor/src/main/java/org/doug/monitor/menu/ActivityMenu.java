package org.doug.monitor.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.doug.monitor.AssemblyTestActivity;
import org.doug.monitor.PerformanceTestActivity;
import org.doug.monitor.base.App;
import org.doug.monitor.R;
import org.doug.monitor.factorytest.FactoryAutoTest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wesine on 2018/5/23.
 */

public class ActivityMenu extends AppCompatActivity {
    private static final Class<?>[] ACTIVITY = {AssemblyTestActivity.class, FactoryAutoTest.class, PerformanceTestActivity.class};
    private static final String[] TITLE = {"组装检测", "老化测试", "电性能测试"};
    private static final int[] IMG = {R.drawable.menu_assembly, R.drawable.menu_aging, R.drawable.menu_performance};

    private RecyclerView mRecyclerView;
    private List<MenuItem> mDataList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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
                startActivity(intent);
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
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataList.clear();
        App.exit();
    }
}
