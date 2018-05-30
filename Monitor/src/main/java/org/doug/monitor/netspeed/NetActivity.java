package org.doug.monitor.netspeed;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;

import org.doug.monitor.Constans;
import org.doug.monitor.R;
import org.doug.monitor.util.Toaster;

/**
 * Created by wesine on 2018/5/23.
 */

public class NetActivity extends AppCompatActivity {
    private int REQUEST_CODE=0;
    private Button showBt;
    private Button closeBt;
    private Button exitBt;
    private RadioButton radioBt1;
    private RadioButton radioBt2;
    private RadioButton radioBt3;




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //检查悬浮窗权限
//        if(checkDrawOverlayPermission()){
            init();
//        }
    }

    private void init() {
        showBt= (Button) findViewById(R.id.bt_show);
        closeBt= (Button) findViewById(R.id.bt_close);
        exitBt= (Button) findViewById(R.id.bt_exit);
        radioBt1= (RadioButton) findViewById(R.id.radio_1);
        radioBt2= (RadioButton) findViewById(R.id.radio_2);
        radioBt3= (RadioButton) findViewById(R.id.radio_3);
        showBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(NetActivity.this,SpeedCalculationService.class));
            }
        });
        closeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(NetActivity.this,SpeedCalculationService.class));
            }
        });
        exitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        WindowUtil.statusBarHeight=getStatusBarHeight();
        String setting= (String) SharedPreferencesUtils.getFromSpfs(this, Constans.SETTING,Constans.BOTH);
        if(setting.equals(Constans.BOTH)){
            radioBt1.setChecked(true);
        }else if(setting.equals(Constans.UP)){
            radioBt2.setChecked(true);
        }else{
            radioBt3.setChecked(true);
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkDrawOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE);
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE){
            if (Settings.canDrawOverlays(this)) {
                init();
            }else{
                Toaster.showToast(this, "请授予悬浮窗权限");
                finish();
            }
        }
    }

    private int getStatusBarHeight(){
        Rect rectangle = new Rect();
        Window window =getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;
        return statusBarHeight;
    }

    public void onRadioButtonClick(View view){
        switch (view.getId()){
            case R.id.radio_1:
                radioBt1.setChecked(true);
                SharedPreferencesUtils.putToSpfs(this,Constans.SETTING,Constans.BOTH);
                break;
            case R.id.radio_2:
                radioBt2.setChecked(true);
                SharedPreferencesUtils.putToSpfs(this,Constans.SETTING,Constans.UP);
                break;
            case R.id.radio_3:
                radioBt3.setChecked(true);
                SharedPreferencesUtils.putToSpfs(this,Constans.SETTING,Constans.DOWN);
                break;
            default:
                break;
        }
        startService(new Intent(this,SpeedCalculationService.class)
                .putExtra(Constans.CHANGED,true));
    }

}
