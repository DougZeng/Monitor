package org.doug.monitor.clean;

import android.os.Bundle;

import org.doug.monitor.R;
import org.doug.monitor.base.BaseActivity;

/**
 * Created by wesine on 2018/6/20.
 */

public class CleanDataActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_data);
        setTitle("设置出厂模式");
        setBackBtn();
    }
}
