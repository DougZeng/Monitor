package org.doug.monitor.base.guid.test;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.doug.monitor.R;

/**
 * Created by wesine on 2018/6/14.
 */

public class GuidActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guid);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new GuideViewFragment());
        transaction.commit();
    }
}
