package org.doug.monitor.base.bluetooth.scan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

/**
 *
 */
public abstract class MacScanCallback implements BluetoothAdapter.LeScanCallback {
    private String mac;

    protected MacScanCallback(String mac) {
        this.mac = mac;
        if (mac == null) throw new IllegalArgumentException("start scan, mac can not be null!");
    }

    public abstract void onMacScaned(BluetoothDevice device, int rssi, byte[] scanRecord);

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if (mac.equalsIgnoreCase(device.getAddress())) {
            onMacScaned(device, rssi, scanRecord);
        }
    }


}
