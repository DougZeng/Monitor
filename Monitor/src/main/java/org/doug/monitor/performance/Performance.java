package org.doug.monitor.performance;


import org.doug.monitor.assembly.Assembly;

/**
 * Created by wesine on 2018/6/15.
 */

public class Performance extends Assembly {

    private int displayVersion;
    private int serialNo;
    private int eth0;
    private int wifi;

    public int getDisplayVersion() {
        return displayVersion;
    }

    public void setDisplayVersion(int displayVersion) {
        this.displayVersion = displayVersion;
    }

    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }

    public int getEth0() {
        return eth0;
    }

    public void setEth0(int eth0) {
        this.eth0 = eth0;
    }

    public int getWifi() {
        return wifi;
    }

    public void setWifi(int wifi) {
        this.wifi = wifi;
    }


    public Performance() {
    }

    public Performance(int visualInspection, int camera, int scanner, int touch, int autoTouch, int audio, int ethernet, int displayVersion, int serialNo, int eth0, int wifi, int factory_mode) {
        super(visualInspection, camera, scanner, touch, autoTouch, audio, ethernet);
        this.displayVersion = displayVersion;
        this.serialNo = serialNo;
        this.eth0 = eth0;
        this.wifi = wifi;
    }
}
