package org.doug.monitor.bluetooth.conn;

/**
 * callback of RSSI read.
 */
public abstract class BleRssiCallback extends BleCallback {
    public abstract void onSuccess(int rssi);
}