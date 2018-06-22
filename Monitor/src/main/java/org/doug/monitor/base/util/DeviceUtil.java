package org.doug.monitor.base.util;

import android.Manifest;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.orhanobut.logger.Logger;

import org.doug.monitor.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.NetworkInterface;

/**
 * Created by wesine on 2018/6/20.
 */

public class DeviceUtil {

    /**
     * 5.1.1
     *
     * @return
     */
    public static String getDeviceVersionRelease() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 22
     *
     * @return
     */
    public static String getVersionSDK() {
        return Build.VERSION.SDK;
    }

    /**
     * @return
     */
    public static String getModel() {
        return Build.MODEL;
    }

    /**
     * @return
     */
    public static String getDisplayVersion() {
        return Build.DISPLAY;
    }

    /**
     * @return
     */
    public static String getSerial() {
        return Build.SERIAL;
    }

    public static String getEth0() {
        String ethernetMacAddress = "";
        try (BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream("/sys/class/net/eth0/address")))) {
            ethernetMacAddress = input.readLine();
            Logger.d("Ethernet MAC Address: " + ethernetMacAddress);
        } catch (IOException ex) {
            Logger.e("ex: " + ex);
        }
        return ethernetMacAddress;
    }

    public static String getWifiMac(Context context) {
//        if (!permissionUtils.isPermissionGranted(Manifest.permission.ACCESS_WIFI_STATE))
//            throw new RuntimeException("Access Wifi state permission not granted!");

        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String address = info.getMacAddress();
        return address;
    }
}
