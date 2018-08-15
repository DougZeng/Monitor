package org.doug.monitor.performance;


import org.doug.monitor.assembly.Assembly;

/**
 * Created by wesine on 2018/6/15.
 */

public class Performance extends Assembly {

    private int displayVersion;
    private int serialNo;
    private int e_mac;
    private int w_mac;

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

    public int getE_mac() {
        return e_mac;
    }

    public void setE_mac(int e_mac) {
        this.e_mac = e_mac;
    }

    public int getW_mac() {
        return w_mac;
    }

    public void setW_mac(int w_mac) {
        this.w_mac = w_mac;
    }

    public Performance() {
    }


}
